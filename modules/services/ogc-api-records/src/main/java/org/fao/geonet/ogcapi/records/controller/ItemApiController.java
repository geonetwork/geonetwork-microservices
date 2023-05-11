/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.fao.geonet.common.search.ElasticSearchProxy;
import org.fao.geonet.common.search.GnMediaType;
import org.fao.geonet.common.search.SearchConfiguration;
import org.fao.geonet.common.search.SearchConfiguration.Format;
import org.fao.geonet.common.search.SearchConfiguration.Operations;
import org.fao.geonet.common.search.domain.es.EsSearchResults;
import org.fao.geonet.domain.Metadata;
import org.fao.geonet.domain.Source;
import org.fao.geonet.index.JsonUtils;
import org.fao.geonet.index.converter.DcatConverter;
import org.fao.geonet.index.converter.SchemaOrgConverter;
import org.fao.geonet.index.model.dcat2.CatalogRecord;
import org.fao.geonet.index.model.dcat2.DataService;
import org.fao.geonet.index.model.dcat2.Dataset;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames;
import org.fao.geonet.ogcapi.records.model.Item;
import org.fao.geonet.ogcapi.records.model.XsltModel;
import org.fao.geonet.ogcapi.records.service.CollectionService;
import org.fao.geonet.ogcapi.records.util.MediaTypeUtil;
import org.fao.geonet.ogcapi.records.util.RecordsEsQueryBuilder;
import org.fao.geonet.ogcapi.records.util.XmlUtil;
import org.fao.geonet.repository.MetadataRepository;
import org.fao.geonet.view.ViewUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import springfox.documentation.annotations.ApiIgnore;


@Api(tags = "OGC API Records")
@Controller
@Slf4j(topic = "org.fao.geonet.ogcapi.records")
public class ItemApiController {

  public static final String EXCEPTION_COLLECTION_NOT_FOUND =
      "ogcapir.exception.collection.notFound";
  public static final String EXCEPTION_COLLECTION_ITEM_NOT_FOUND =
      "ogcapir.exception.collectionItem.notFound";
  @Autowired
  ElasticSearchProxy proxy;
  @Autowired
  MetadataRepository metadataRepository;
  @Autowired
  ViewUtility viewUtility;
  @Autowired
  @Qualifier("xsltViewResolver")
  ViewResolver viewResolver;
  @Autowired
  CollectionService collectionService;
  @Autowired
  MessageSource messages;
  @Autowired
  RecordsEsQueryBuilder recordsEsQueryBuilder;
  @Autowired
  SearchConfiguration searchConfiguration;
  @Autowired
  MediaTypeUtil mediaTypeUtil;
  @Autowired
  DcatConverter dcatConverter;

  /**
   * Describe a collection item.
   *
   */
  @io.swagger.v3.oas.annotations.Operation(
      summary = "Describe a collection item.",
      description = "Collection Information is the set of metadata that describes a "
          + "single collection. An abbreviated copy of this information is returned for each "
          + "Collection in the /collections response.")
  @GetMapping(value = "/collections/{collectionId}/items/{recordId}",
      produces = {MediaType.APPLICATION_JSON_VALUE,
          MediaType.TEXT_HTML_VALUE,
          MediaType.APPLICATION_RSS_XML_VALUE,
          MediaType.APPLICATION_ATOM_XML_VALUE,
          MediaType.APPLICATION_XML_VALUE,
          GnMediaType.APPLICATION_JSON_LD_VALUE,
          GnMediaType.APPLICATION_RDF_XML_VALUE,
          GnMediaType.APPLICATION_DCAT2_XML_VALUE,
          GnMediaType.TEXT_TURTLE_VALUE,
          GnMediaType.APPLICATION_GEOJSON_VALUE})
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Describe a collection item.")
  })
  public ResponseEntity<Void> collectionsCollectionIdItemsRecordIdGet(
      @ApiParam(value = "Identifier (name) of a specific collection", required = true)
      @PathVariable("collectionId") String collectionId,
      @ApiParam(value = "Identifier (name) of a specific record", required = true)
      @PathVariable("recordId")String recordId,
      @ApiIgnore HttpServletRequest request,
      @ApiIgnore HttpServletResponse response,
      @ApiIgnore Model model) {

    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          messages.getMessage(EXCEPTION_COLLECTION_NOT_FOUND,
              new String[]{collectionId},
              request.getLocale()));
    }

    List<MediaType> allowedMediaTypes =
        ListUtils.union(MediaTypeUtil.defaultSupportedMediaTypes,
            MediaTypeUtil.ldSupportedMediaTypes);
    allowedMediaTypes.add(GnMediaType.APPLICATION_GEOJSON);

    MediaType mediaType =
        mediaTypeUtil.calculatePriorityMediaTypeFromRequest(request, allowedMediaTypes);


    if (mediaType.equals(MediaType.APPLICATION_JSON)
        || mediaType.equals(GnMediaType.APPLICATION_GEOJSON)) {
      try {
        String type = mediaType.equals(MediaType.APPLICATION_JSON) ? "json" : "geojson";
        JsonNode recordAsJson = getRecordAsJson(collectionId, recordId, request, source, type);

        streamResult(response,
            recordAsJson.toPrettyString(),
            MediaType.APPLICATION_JSON_VALUE);
        return ResponseEntity.ok().build();
      } catch (Exception ex) {
        log.error(String.format(
            "An error occurred while describing a collection item '%s'. Error is: %s",
            collectionId, ex.getMessage()));
        throw new RuntimeException(ex);
      }

    } else if (MediaTypeUtil.ldSupportedMediaTypes.contains(mediaType)) {
      return collectionsCollectionIdItemsRecordIdGetAsJsonLd(collectionId, recordId,
          mediaType.toString(), request, response);

    } else if (MediaTypeUtil.xmlMediaTypes.contains(mediaType)) {
      return collectionsCollectionIdItemsRecordIdGetAsXml(collectionId, recordId,
          request, response);

    } else {
      return collectionsCollectionIdItemsRecordIdGetAsHtml(collectionId, recordId,
          request, response, model);
    }
  }


  /**
   * Describe the collection items.
   *
   */
  @io.swagger.v3.oas.annotations.Operation(
      summary = "Describe the collection items.",
      description = "Collection Information is the set of metadata that describes a "
          + "single collection. An abbreviated copy of this information is returned for each "
          + "Collection in the /collections response.")
  @GetMapping(value = "/collections/{collectionId}/items",
      produces = {
          MediaType.APPLICATION_XML_VALUE,
          MediaType.APPLICATION_JSON_VALUE,
          GnMediaType.APPLICATION_JSON_LD_VALUE,
          MediaType.APPLICATION_RSS_XML_VALUE,
          GnMediaType.APPLICATION_RDF_XML_VALUE,
          GnMediaType.APPLICATION_DCAT2_XML_VALUE,
          MediaType.TEXT_HTML_VALUE,
          GnMediaType.APPLICATION_GEOJSON_VALUE,
      })
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Describe a collection item.")
  })
  public ResponseEntity<Void> collectionsCollectionIdItemsGet(
      @ApiParam(value = "Identifier (name) of a specific collection", required = true)
      @PathVariable("collectionId")
          String collectionId,
      @ApiParam(value = "")
      @RequestParam(value = "bbox", required = false)
          List<BigDecimal> bbox,
      @ApiParam(value = "")
      @RequestParam(value = "datetime", required = false)
          String datetime,
      @ApiParam(value = "", defaultValue = "10")
      @RequestParam(value = "limit", required = false, defaultValue = "10")
          Integer limit,
      @ApiParam(value = "", defaultValue = "0")
      @RequestParam(value = "startindex", required = false, defaultValue = "0")
          Integer startindex,
      @ApiParam(value = "")
      @RequestParam(value = "type", required = false)
          String type,
      @ApiParam(value = "")
      @RequestParam(value = "q", required = false)
          List<String> q,
      @ApiParam(value = "")
      @RequestParam(value = "externalids", required = false)
          List<String> externalids,
      @ApiParam(value = "")
      @RequestParam(value = "sortby", required = false)
          List<String> sortby,
      @ApiIgnore HttpServletRequest request,
      @ApiIgnore HttpServletResponse response,
      @ApiIgnore Model model) throws Exception {

    List<MediaType> allowedMediaTypes =
        ListUtils.union(MediaTypeUtil.defaultSupportedMediaTypes,
            Arrays.asList(
                GnMediaType.APPLICATION_JSON_LD,
                MediaType.APPLICATION_RSS_XML,
                GnMediaType.APPLICATION_RDF_XML,
                GnMediaType.APPLICATION_DCAT2_XML,
                GnMediaType.APPLICATION_GEOJSON));
    MediaType mediaType =
        mediaTypeUtil.calculatePriorityMediaTypeFromRequest(request, allowedMediaTypes);

    if (mediaType.equals(MediaType.APPLICATION_XML)
        || mediaType.equals(MediaType.APPLICATION_JSON)
        || mediaType.equals(GnMediaType.APPLICATION_JSON_LD)
        || mediaType.equals(GnMediaType.APPLICATION_DCAT2_XML)
        || mediaType.equals(GnMediaType.APPLICATION_RDF_XML)
        || mediaType.equals(MediaType.APPLICATION_RSS_XML)
        || mediaType.equals(GnMediaType.APPLICATION_GEOJSON)) {

      boolean allSourceFields =
          mediaType.equals(GnMediaType.APPLICATION_DCAT2_XML)
          || mediaType.equals(GnMediaType.APPLICATION_RDF_XML);

      return collectionsCollectionIdItemsGetInternal(
          collectionId, bbox, datetime, limit, startindex, type, q, externalids, sortby,
          request, response, allSourceFields);

    } else {
      return collectionsCollectionIdItemsGetAsHtml(collectionId, bbox, datetime, limit,
          startindex, type, q, externalids, sortby, request, response, model);
    }
  }


  private ResponseEntity<Void> collectionsCollectionIdItemsRecordIdGetAsJsonLd(
      String collectionId,
      String recordId,
      String acceptHeader,
      HttpServletRequest request,
      HttpServletResponse response) {

    Source source = collectionService.retrieveSourceForCollection(collectionId);

    try {
      String formatParameter = request.getParameter("f");
      boolean isTurtle =
          (formatParameter != null && "dcat_turtle".equals(formatParameter))
              || GnMediaType.TEXT_TURTLE_VALUE.equals(acceptHeader);
      boolean isDcat =
          (formatParameter != null && "dcat".equals(formatParameter))
              || GnMediaType.TEXT_TURTLE_VALUE.equals(acceptHeader);
      boolean isRdfXml =
          (formatParameter != null && "rdfxml".equals(formatParameter))
              || GnMediaType.APPLICATION_RDF_XML_VALUE.equals(acceptHeader);
      boolean isLinkedData = (isTurtle || isRdfXml || isDcat);

      JsonNode recordAsJson = getRecordAsJson(collectionId, recordId, request, source,
          isLinkedData ? "json" : "schema.org");

      if (isLinkedData) {
        JAXBContext context = null;
        context = JAXBContext.newInstance(
            CatalogRecord.class, Dataset.class, DataService.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        CatalogRecord catalogRecord = dcatConverter.convert(recordAsJson);
        StringWriter sw = new StringWriter();
        marshaller.marshal(catalogRecord, sw);
        String dcatXml = sw.toString();

        if (isTurtle) {
          org.eclipse.rdf4j.model.Model model = Rio.parse(
              new ByteArrayInputStream(dcatXml.getBytes()),
              "", RDFFormat.RDFXML);
          StringWriter turtleWriter = new StringWriter();
          Rio.write(model, turtleWriter, RDFFormat.TURTLE);
          streamResult(response,
              turtleWriter.toString(),
              GnMediaType.TEXT_TURTLE_VALUE);
        } else {
          streamResult(response,
              dcatXml,
              MediaType.APPLICATION_XML_VALUE);
        }

      } else {
        streamResult(response,
            recordAsJson.toString(),
            GnMediaType.APPLICATION_JSON_LD_VALUE);
      }
      return ResponseEntity.ok().build();
    } catch (Exception ex) {
      log.error(String.format(
          "An error occurred while building JSON-LD representation of collection '%s'. "
              + "Error is: %s",
          source.getName(), ex.getMessage()));
      throw new RuntimeException(ex);
    }

  }


  private ResponseEntity<Void> collectionsCollectionIdItemsRecordIdGetAsXml(
      String collectionId,
      String recordId,
      HttpServletRequest request,
      HttpServletResponse response) {

    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          messages.getMessage(EXCEPTION_COLLECTION_NOT_FOUND,
              new String[]{collectionId},
              request.getLocale()));
    }

    try {
      String collectionFilter = collectionService.retrieveCollectionFilter(source, true);
      String query = recordsEsQueryBuilder.buildQuerySingleRecord(recordId, collectionFilter, null);

      String queryResponse = proxy.searchAndGetResult(request.getSession(), request, query, null);

      Document queryResult = XmlUtil.parseXmlString(queryResponse);
      String total = queryResult.getChildNodes().item(0).getAttributes().getNamedItem("total")
          .getNodeValue();

      if (Integer.parseInt(total) == 0) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            messages.getMessage(EXCEPTION_COLLECTION_ITEM_NOT_FOUND,
                new String[]{recordId, collectionId},
                request.getLocale()));
      }

      Node metadataResult = queryResult.getChildNodes().item(0).getFirstChild();

      streamResult(response, XmlUtil.getNodeString(metadataResult),
          MediaType.APPLICATION_XML_VALUE);

      return ResponseEntity.ok().build();
    } catch (Exception ex) {
      log.error(String.format(
          "An error occurred while building XML representation of collection '%s'. Error is: %s",
          source.getName(), ex.getMessage()));
      throw new RuntimeException(ex);
    }
  }


  private ResponseEntity<Void> collectionsCollectionIdItemsRecordIdGetAsHtml(
      String collectionId,
      String recordId,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model) {
    Locale locale = LocaleContextHolder.getLocale();
    String language = locale.getISO3Language();
    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find collection");
    }

    try {
      JsonNode recordAsJson = getRecordAsJson(collectionId, recordId, request, source, "json");

      Metadata metadataRecord = metadataRepository.findOneByUuid(recordId);
      if (metadataRecord == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            messages.getMessage(EXCEPTION_COLLECTION_ITEM_NOT_FOUND,
                new String[]{recordId, collectionId},
                request.getLocale()));
      }

      XsltModel modelSource = new XsltModel();
      try {
        IndexRecord recordPojo = JsonUtils.getObjectMapper().readValue(
            recordAsJson.get(IndexRecordFieldNames.source).toPrettyString(),
            IndexRecord.class);
        modelSource.setSeoJsonLdSnippet(
            SchemaOrgConverter.convert(recordPojo).toString());
      } catch (Exception e) {
        log.error(String.format(
            "An error occurred while building JSON-LD representation of record '%s'. Error is: %s",
            recordId, e.getMessage()));
      }
      modelSource.setRequestParameters(request.getParameterMap());
      modelSource.setOutputFormats(searchConfiguration.getFormats(Operations.item));
      modelSource.setCollection(source);
      modelSource.setItems(List.of(
          new Item(recordId, null, metadataRecord.getData())
      ));

      model.addAttribute("source", modelSource.toSource());
      viewUtility.addi18n(model, locale, List.of(metadataRecord.getDataInfo().getSchemaId()),
          request);

      View view = viewResolver.resolveViewName("ogcapir/item", locale);
      view.render(model.asMap(), request, response);

      return ResponseEntity.ok().build();
    } catch (Exception ex) {
      log.error(String.format(
          "An error occurred while building HTML representation of collection '%s'. Error is: %s",
          source.getName(), ex.getMessage()));
      throw new RuntimeException(ex);
    }
  }


  private JsonNode getRecordAsJson(
      String collectionId,
      String recordId,
      HttpServletRequest request,
      Source source,
      String type) throws Exception {
    String collectionFilter = collectionService.retrieveCollectionFilter(source, true);
    String query = recordsEsQueryBuilder.buildQuerySingleRecord(recordId, collectionFilter, null);

    String queryResponse = proxy.searchAndGetResult(request.getSession(), request, query, null);

    ObjectMapper mapper = new ObjectMapper();
    JsonFactory factory = mapper.getFactory();
    JsonParser parser = factory.createParser(queryResponse);
    JsonNode actualObj = mapper.readTree(parser);

    JsonNode totalValue =
        "json".equals(type)
            ? actualObj.get("hits").get("total").get("value")
            : actualObj.get("size");

    if ((totalValue == null) || (totalValue.intValue() == 0)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          messages.getMessage(EXCEPTION_COLLECTION_ITEM_NOT_FOUND,
              new String[]{recordId, collectionId},
              request.getLocale()));
    }

    if ("json".equals(type)) {
      return actualObj.get("hits").get("hits").get(0);
    } else {
      String elementName = "schema.org".equals(type) ? "dataFeedElement" : "features";
      return actualObj.get(elementName).get(0);
    }
  }


  private List<String> setDefaultRssSortBy(List<String> sortby, HttpServletRequest request) {
    boolean isRss = "rss".equals(request.getParameter("f"))
        || (request.getHeader(HttpHeaders.ACCEPT) != null
            && request.getHeader(HttpHeaders.ACCEPT).contains(MediaType.APPLICATION_RSS_XML_VALUE));
    if (isRss
        && (sortby == null || sortby.isEmpty())) {
      sortby = new ArrayList<>();
      sortby.add("-" + IndexRecordFieldNames.dateStamp);
    }
    return sortby;
  }


  private String search(
      String collectionId,
      List<BigDecimal> bbox,
      String datetime,
      Integer limit,
      Integer startindex,
      String type,
      List<String> q,
      List<String> externalids,
      List<String> sortby,
      HttpServletRequest request, boolean allSourceFields) {

    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find collection");
    }

    String collectionFilter = collectionService.retrieveCollectionFilter(source, false);
    String query = recordsEsQueryBuilder
        .buildQuery(q, externalids, bbox,
            startindex, limit, collectionFilter, sortby,
            allSourceFields ? Set.of("*") : null);
    try {
      return proxy.searchAndGetResult(request.getSession(), request, query, null);
    } catch (Exception ex) {
      log.error(String.format(
          "An error occurred while searching. Error is: %s",
          ex.getMessage()));

      throw new RuntimeException(ex);
    }
  }


  private ResponseEntity<Void> collectionsCollectionIdItemsGetInternal(
      String collectionId,
      List<BigDecimal> bbox,
      String datetime,
      Integer limit,
      Integer startindex,
      String type,
      List<String> q,
      List<String> externalids,
      List<String> sortby,
      HttpServletRequest request,
      HttpServletResponse response,
      boolean allSourceFields) {

    sortby = setDefaultRssSortBy(sortby, request);

    String queryResponse = search(collectionId, bbox, datetime, limit, startindex, type, q,
        externalids, sortby, request, allSourceFields);

    try {
      streamResult(response, queryResponse, getResponseContentType(request));
    } catch (IOException ioException) {
      throw new RuntimeException(ioException);
    }

    return ResponseEntity.ok().build();
  }


  /**
   * Collection items as HTML.
   */
  private ResponseEntity<Void> collectionsCollectionIdItemsGetAsHtml(
      String collectionId,
      List<BigDecimal> bbox,
      String datetime,
      Integer limit,
      Integer startindex,
      String type,
      List<String> q,
      List<String> externalids,
      List<String> sortby,
      HttpServletRequest request,
      HttpServletResponse response,
      Model model) throws Exception {

    Locale locale = LocaleContextHolder.getLocale();
    String language = locale.getISO3Language();
    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find collection");
    }

    String collectionFilter = collectionService.retrieveCollectionFilter(source, false);
    String query = recordsEsQueryBuilder
        .buildQuery(q, externalids, bbox, startindex, limit, collectionFilter, sortby, null);
    EsSearchResults results = new EsSearchResults();
    try {
      results = proxy
          .searchAndGetResultAsObject(request.getSession(), request, query, null);
    } catch (Exception ex) {
      // TODO: Log exception
      throw new RuntimeException(ex);
    }

    XsltModel modelSource = new XsltModel();
    Map<String, String[]> parameterMap = new HashMap<>(request.getParameterMap());
    if (request.getParameter("limit") == null) {
      parameterMap.put("limit", new String[]{limit + ""});
    }
    if (request.getParameter("startindex") == null) {
      parameterMap.put("startindex", new String[]{startindex + ""});
    }
    modelSource.setRequestParameters(parameterMap);
    modelSource.setCollection(source);
    modelSource.setResults(results);
    modelSource.setOutputFormats(searchConfiguration.getFormats(Operations.items));
    model.addAttribute("source", modelSource.toSource());
    viewUtility.addi18n(model, locale, request);

    View view = viewResolver.resolveViewName("ogcapir/collection", locale);
    view.render(model.asMap(), request, response);

    return ResponseEntity.ok().build();
  }


  /**
   * Streams the content body in the response stream using the content-type provided.
   */
  private void streamResult(
      HttpServletResponse response,
      String content,
      String contentType) throws IOException {
    PrintWriter out = response.getWriter();
    try {
      response.setContentType(contentType);
      response.setCharacterEncoding("UTF-8");
      out.print(content);
    } finally {
      out.flush();
    }
  }


  /**
   * Calculates the response content type.
   */
  private String getResponseContentType(HttpServletRequest request) {
    String mediaType = "";
    String formatParam = request.getParameter("f");

    if (StringUtils.isNotEmpty(formatParam)) {
      Optional<Format> format = searchConfiguration.getFormats()
          .stream().filter(f -> f.getName().equals(formatParam)).findFirst();

      if (format.isPresent()) {
        mediaType = format.get().getMimeType();
      }
    } else {
      mediaType = request.getHeader("Accept");
    }

    if (StringUtils.isEmpty(mediaType)) {
      mediaType = MediaType.APPLICATION_JSON_VALUE;
    }

    return mediaType;
  }
}