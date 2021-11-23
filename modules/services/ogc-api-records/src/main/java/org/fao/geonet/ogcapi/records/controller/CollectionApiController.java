package org.fao.geonet.ogcapi.records.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.apache.commons.collections.ListUtils;
import org.fao.geonet.common.search.GnMediaType;
import org.fao.geonet.common.search.SearchConfiguration;
import org.fao.geonet.common.search.SearchConfiguration.Operations;
import org.fao.geonet.domain.Source;
import org.fao.geonet.index.model.opensearch.OpenSearchDescription;
import org.fao.geonet.index.model.opensearch.OpenSearchDescription.Url;
import org.fao.geonet.ogcapi.records.controller.model.CollectionInfo;
import org.fao.geonet.ogcapi.records.model.XsltModel;
import org.fao.geonet.ogcapi.records.service.CollectionService;
import org.fao.geonet.ogcapi.records.util.CollectionInfoBuilder;
import org.fao.geonet.ogcapi.records.util.MediaTypeUtil;
import org.fao.geonet.view.ViewUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import springfox.documentation.annotations.ApiIgnore;


@Api(tags = "OGC API Records")
@Controller
public class CollectionApiController {

  @Value("${gn.baseurl}")
  String baseUrl;

  @Autowired
  SearchConfiguration searchConfiguration;

  @Autowired
  ViewUtility viewUtility;

  @Autowired
  @Qualifier("xsltViewResolver")
  ViewResolver viewResolver;

  @Autowired
  private CollectionService collectionService;

  @Autowired
  MessageSource messages;

  @Autowired
  private SearchConfiguration configuration;

  @Autowired
  MediaTypeUtil mediaTypeUtil;

  /**
   * Describe a collection.
   */
  @io.swagger.v3.oas.annotations.Operation(
      summary = "Describe a collection.",
      description = "Collection Information is the set of metadata that describes a "
          + "single collection. An abbreviated copy of this information is returned for each "
          + "Collection in the /collections response.")
  @GetMapping(value = "/collections/{collectionId}",
      produces = {MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE,
          MediaType.TEXT_HTML_VALUE,
          MediaType.APPLICATION_RSS_XML_VALUE,
          MediaType.APPLICATION_ATOM_XML_VALUE,
          GnMediaType.APPLICATION_OPENSEARCH_XML_VALUE})
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Describe a collection.")
  })
  @ResponseBody
  public ResponseEntity<CollectionInfo> describeCollection(
      @ApiParam(value = "Identifier (name) of a specific collection", required = true)
      @PathVariable("collectionId") String collectionId,
      @ApiIgnore HttpServletRequest request,
      @ApiIgnore HttpServletResponse response,
      @ApiIgnore Model model) throws Exception {

    Locale locale = LocaleContextHolder.getLocale();
    String language = locale.getISO3Language();

    List<MediaType> allowedMediaTypes =
        ListUtils.union(MediaTypeUtil.defaultSupportedMediaTypes,
            MediaTypeUtil.openSearchSupportedMediaTypes);

    MediaType mediaType =
        mediaTypeUtil.calculatePriorityMediaTypeFromRequest(request, allowedMediaTypes);

    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          messages.getMessage("ogcapir.exception.collection.notFound",
              new String[]{collectionId},
              request.getLocale()));
    }

    if (MediaTypeUtil.defaultSupportedMediaTypes.contains(mediaType)) {
      if (!mediaType.equals(MediaType.TEXT_HTML)) {
        String baseUrl = request.getRequestURL()
            .toString().replace(collectionId, "");

        CollectionInfo collectionInfo = CollectionInfoBuilder
            .buildFromSource(source, language, baseUrl, mediaType);

        return ResponseEntity.ok(collectionInfo);

      } else {
        XsltModel modelSource = new XsltModel();
        modelSource.setOutputFormats(configuration.getFormats(Operations.collection));
        modelSource.setCollection(source);
        model.addAttribute("source", modelSource.toSource());

        viewUtility.addi18n(model, locale, request);

        View view = viewResolver.resolveViewName("ogcapir/collection", locale);
        view.render(model.asMap(), request, response);

        return ResponseEntity.ok(new CollectionInfo());
      }

    } else {
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
      return ResponseEntity.ok(new CollectionInfo());
    }
  }


  /**
   * Describe of the sorting capabilities offered on a collection.
   */
  @io.swagger.v3.oas.annotations.Operation(
      summary = "Describe of the sorting capabilities offered on a collection.",
      description = "Collection Information is the set of metadata that describes a "
          + "single collection. An abbreviated copy of this information is returned for each "
          + "Collection in the /collections response.")
  @GetMapping(value = "/collections/{collectionId}/sortables",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  @ResponseBody
  public ResponseEntity<List<String>> getCollectionSortables(
      @ApiParam(value = "Identifier (name) of a specific collection", required = true)
      @PathVariable("collectionId") String collectionId,
      @ApiIgnore HttpServletRequest request) {
    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          messages.getMessage("ogcapir.exception.collection.notFound",
              new String[]{collectionId},
              request.getLocale()));
    }

    return ResponseEntity.ok(collectionService.getSortables(source));
  }
}
