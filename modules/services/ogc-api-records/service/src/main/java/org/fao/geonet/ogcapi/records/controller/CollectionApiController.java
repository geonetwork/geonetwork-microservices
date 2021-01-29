package org.fao.geonet.ogcapi.records.controller;

import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.fao.geonet.common.search.GnMediaType;
import org.fao.geonet.common.search.SearchConfiguration;
import org.fao.geonet.domain.Source;
import org.fao.geonet.index.model.opensearch.OpenSearchDescription;
import org.fao.geonet.index.model.opensearch.OpenSearchDescription.Url;
import org.fao.geonet.ogcapi.records.CollectionApi;
import org.fao.geonet.ogcapi.records.OgcApiConfiguration;
import org.fao.geonet.ogcapi.records.model.XsltModel;
import org.fao.geonet.ogcapi.records.rest.ogc.model.CollectionInfo;
import org.fao.geonet.ogcapi.records.service.CollectionService;
import org.fao.geonet.ogcapi.records.util.CollectionInfoBuilder;
import org.fao.geonet.ogcapi.records.util.MediaTypeUtil;
import org.fao.geonet.view.ViewUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.HttpResource;


@Controller
public class CollectionApiController implements CollectionApi {

  @Value("${gn.baseurl}")
  String baseUrl;

  @Autowired
  SearchConfiguration searchConfiguration;
  @Autowired
  ViewUtility viewUtility;
  @Autowired
  private NativeWebRequest nativeWebRequest;
  @Autowired
  private CollectionService collectionService;
  @Autowired
  MessageSource messages;
  @Autowired
  private OgcApiConfiguration configuration;

  /**
   * Describe a collection.
   */
  @Override
  public ResponseEntity<CollectionInfo> describeCollection(
      @ApiParam(value = "Identifier (name) of a specific collection", required = true)
      @PathVariable("collectionId") String collectionId) {

    Locale locale = LocaleContextHolder.getLocale();
    String language = locale.getISO3Language();

    MediaType mediaType = MediaTypeUtil.calculatePriorityMediaTypeFromRequest(nativeWebRequest);

    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          messages.getMessage("ogcapir.exception.collection.notFound",
              new String[]{collectionId},
              ((HttpServletRequest) nativeWebRequest.getNativeRequest()).getLocale()));
    }

    String baseUrl = ((HttpServletRequest) nativeWebRequest.getNativeRequest()).getRequestURL()
        .toString().replace(collectionId, "");

    CollectionInfo collectionInfo = CollectionInfoBuilder
        .buildFromSource(source, language, baseUrl, mediaType);

    return ResponseEntity.ok(collectionInfo);
  }

  /**
   * Collections as XML.
   */
  @GetMapping(value = "/collections/{collectionId}",
      produces = {
        MediaType.APPLICATION_XML_VALUE
      })
  public ResponseEntity<CollectionInfo> describeCollectionAsXml(
      @PathVariable("collectionId") String collectionId) {
    return describeCollection(collectionId);
  }

  /**
   * Collection as HTML.
   */
  @GetMapping(value = "/collections/{collectionId}",
      produces = {
        MediaType.TEXT_HTML_VALUE
      })
  public String describeCollectionAsHtml(
      @PathVariable("collectionId") String collectionId,
      HttpServletRequest request,
      Model model) {
    Source source = collectionService.retrieveSourceForCollection(collectionId);
    XsltModel modelSource = new XsltModel();
    modelSource.setOutputFormats(configuration.getFormats());
    modelSource.setCollection(source);
    model.addAttribute("source", modelSource.toSource());
    Locale locale = LocaleContextHolder.getLocale();
    viewUtility.addi18n(model, locale, request);
    return "ogcapir/collection";
  }


  /**
   * Collections as OpenSearch.
   *
   * <p>See https://github.com/dewitt/opensearch/blob/master/opensearch-1-1-draft-6.md#the-opensearchdescription-element
   */
  @RequestMapping(value = "/collections/{collectionId}",
      produces = {
          MediaType.APPLICATION_RSS_XML_VALUE,
          MediaType.APPLICATION_ATOM_XML_VALUE,
          GnMediaType.APPLICATION_OPENSEARCH_XML_VALUE
      },
      method = RequestMethod.GET)
  @ResponseBody
  public String describeCollectionsAsOpenSearch(
      @PathVariable("collectionId") String collectionId,
      HttpServletResponse response
  ) {
    Locale locale = LocaleContextHolder.getLocale();
    Source source = collectionService.retrieveSourceForCollection(collectionId);
    OpenSearchDescription openSearchDescription = new OpenSearchDescription();
    String label = source.getLabel(locale.getISO3Language());
    String[] labelAndDescription = label.split("\\|");

    //Contains a brief human-readable title that identifies this search engine.
    openSearchDescription.setShortName(labelAndDescription[0]);
    if (labelAndDescription.length == 2) {
      // Contains a human-readable text description of the search engine.
      openSearchDescription.setDescription(labelAndDescription[1]);
    }

    // Describes an interface by which a client can make requests
    // for an external resource, such as search results,
    // search suggestions, or additional description documents.
    searchConfiguration.getFormats().forEach(f -> {
      Url url = new Url();
      url.setType(f.getMimeType());
      url.setIndexOffset(BigInteger.valueOf(0));
      url.setTemplate(String.format(
          "%s/collections/%s/items?f=%s&q={searchTerms}&startIndex={startIndex?}",
          baseUrl,
          source.getUuid(),
          f.getName()));
      openSearchDescription.getUrl().add(url);
    });


    // Does not return document with namespace ? TODO
    //    return openSearchDescription;
    try {
      JAXBContext context = JAXBContext.newInstance(OpenSearchDescription.class);
      StringWriter sw = new StringWriter();
      Marshaller marshaller = context.createMarshaller();
      response.setContentType(GnMediaType.APPLICATION_OPENSEARCH_XML_VALUE);
      response.setHeader(
          "Content-Disposition",
          String.format("attachment; filename=\"collection-%s-opensearch-description.xml\"",
              collectionId));
      marshaller.marshal(openSearchDescription, response.getWriter());
    } catch (JAXBException | IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  @Override
  public ResponseEntity<List<Object>> getCoverageOffering(String collectionId) {
    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          messages.getMessage("ogcapir.exception.collection.notFound",
              new String[]{collectionId},
              ((HttpServletRequest) nativeWebRequest.getNativeRequest()).getLocale()));
    }

    return ResponseEntity.ok(collectionService.getSortables(source));
  }
}
