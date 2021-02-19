/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.controller;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.catalog.Catalog;
import lombok.extern.slf4j.Slf4j;
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
import org.fao.geonet.index.model.dcat2.SkosConcept;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames;
import org.fao.geonet.ogcapi.records.RecordApi;
import org.fao.geonet.ogcapi.records.model.Item;
import org.fao.geonet.ogcapi.records.model.XsltModel;
import org.fao.geonet.ogcapi.records.service.CollectionService;
import org.fao.geonet.ogcapi.records.util.RecordsEsQueryBuilder;
import org.fao.geonet.ogcapi.records.util.XmlUtil;
import org.fao.geonet.repository.MetadataRepository;
import org.fao.geonet.view.ViewUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Controller
@Slf4j(topic = "org.fao.geonet.ogcapi.records")
public class ItemApiController implements RecordApi {

  @Autowired
  ElasticSearchProxy proxy;
  @Autowired
  MetadataRepository metadataRepository;
  @Autowired
  ViewUtility viewUtility;
  @Autowired
  CollectionService collectionService;
  @Autowired
  MessageSource messages;
  @Autowired
  RecordsEsQueryBuilder recordsEsQueryBuilder;
  @Autowired
  SearchConfiguration searchConfiguration;

  /**
   * Only to support sample responses from {@link RecordApi}, remove once all its methods are
   * implemented.
   */
  @Autowired
  private NativeWebRequest nativeWebRequest;

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.of(nativeWebRequest);
  }


  @Override
  public ResponseEntity<Void> collectionsCollectionIdItemsRecordIdGet(
      String collectionId,
      String recordId) {

    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          messages.getMessage("ogcapir.exception.collection.notFound",
              new String[]{collectionId},
              ((HttpServletRequest) nativeWebRequest.getNativeRequest()).getLocale()));
    }

    HttpServletRequest request = ((HttpServletRequest) nativeWebRequest.getNativeRequest());
    HttpServletResponse response = ((HttpServletResponse) nativeWebRequest.getNativeResponse());

    try {
      JsonNode record = getRecordAsJson(collectionId, recordId, request, source, "json");

      streamResult(response,
          record.toPrettyString(),
          MediaType.APPLICATION_JSON_VALUE);
      return ResponseEntity.ok().build();
    } catch (Exception ex) {
      // TODO: Log exception
      throw new RuntimeException(ex);
    }

  }

  /**
   * Collection item as DCAT (RDF/XML or Turtle).
   */
  @GetMapping(
      value = "/collections/{collectionId}/items/{recordId}",
      produces = {
          GnMediaType.APPLICATION_DCAT2_XML_VALUE,
          GnMediaType.TEXT_TURTLE_VALUE,
          GnMediaType.APPLICATION_RDF_XML_VALUE
      })
  public ResponseEntity<Void> collectionsCollectionIdItemsRecordIdGetAsDcat(
      @ApiParam(value = "Identifier (name) of a specific collection", required = true)
      @PathVariable("collectionId")
          String collectionId,
      @ApiParam(value = "Identifier (name) of a specific record", required = true)
      @PathVariable("recordId")
          String recordId,
      @RequestHeader("Accept")
          String acceptHeader,
      HttpServletRequest request,
      HttpServletResponse response) {
    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          messages.getMessage("ogcapir.exception.collection.notFound",
              new String[]{collectionId},
              ((HttpServletRequest) nativeWebRequest.getNativeRequest()).getLocale()));
    }

    try {

      JAXBContext context = null;
      context = JAXBContext.newInstance(
          CatalogRecord.class, Dataset.class, DataService.class);
      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

      JsonNode record = getRecordAsJson(collectionId, recordId, request, source, "json");
      CatalogRecord catalogRecord = DcatConverter.convert(record);
      StringWriter sw = new StringWriter();
      marshaller.marshal(catalogRecord, sw);
      String dcatXml = sw.toString();

      String formatParameter = request.getParameter("f");
      boolean isTurtle =
          (formatParameter != null && "turtle".equals(formatParameter))
              || GnMediaType.TEXT_TURTLE_VALUE.equals(acceptHeader);
      boolean isRdfXml =
          (formatParameter != null && "rdfxml".equals(formatParameter))
              || GnMediaType.APPLICATION_RDF_XML_VALUE.equals(acceptHeader);
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
      return ResponseEntity.ok().build();
    } catch (Exception ex) {
      // TODO: Log exception
      throw new RuntimeException(ex);
    }

  }

  /**
   * Collection item as JSON.
   */
  @GetMapping(
      value = "/collections/{collectionId}/items/{recordId}",
      produces = {
          GnMediaType.APPLICATION_JSON_LD_VALUE
      })
  public ResponseEntity<Void> collectionsCollectionIdItemsRecordIdGetAsJsonLd(
      @ApiParam(value = "Identifier (name) of a specific collection", required = true)
      @PathVariable("collectionId")
          String collectionId,
      @ApiParam(value = "Identifier (name) of a specific record", required = true)
      @PathVariable("recordId")
          String recordId,
      @RequestHeader("Accept")
          String acceptHeader,
      HttpServletRequest request,
      HttpServletResponse response) {
    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          messages.getMessage("ogcapir.exception.collection.notFound",
              new String[]{collectionId},
              ((HttpServletRequest) nativeWebRequest.getNativeRequest()).getLocale()));
    }

    try {
      JsonNode record = getRecordAsJson(collectionId, recordId, request, source, "schema.org");

      streamResult(response,
          record.toString(),
          GnMediaType.APPLICATION_JSON_LD_VALUE);
      return ResponseEntity.ok().build();
    } catch (Exception ex) {
      // TODO: Log exception
      throw new RuntimeException(ex);
    }

  }

  private JsonNode getRecordAsJson(
      String collectionId,
      String recordId,
      HttpServletRequest request,
      Source source,
      String type) throws Exception {
    String collectionFilter = collectionService.retrieveCollectionFilter(source);
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
          messages.getMessage("ogcapir.exception.collectionItem.notFound",
              new String[]{recordId, collectionId},
              ((HttpServletRequest) nativeWebRequest.getNativeRequest()).getLocale()));
    }

    return "json".equals(type)
        ? actualObj.get("hits").get("hits").get(0)
        : actualObj.get("dataFeedElement").get(0);
  }

  /**
   * Collection item as XML / DCAT.
   */
  @GetMapping(
      value = "/collections/{collectionId}/items/{recordId}",
      produces = {
          MediaType.APPLICATION_XML_VALUE,
          GnMediaType.APPLICATION_GN_XML_VALUE,
          GnMediaType.APPLICATION_DCAT2_XML_VALUE
      })
  public ResponseEntity<Void> collectionsCollectionIdItemsRecordIdGetAsXml(
      @ApiParam(value = "Identifier (name) of a specific collection", required = true)
      @PathVariable("collectionId")
          String collectionId,
      @ApiParam(value = "Identifier (name) of a specific record", required = true)
      @PathVariable("recordId")
          String recordId,
      HttpServletRequest request,
      HttpServletResponse response) {

    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
          messages.getMessage("ogcapir.exception.collection.notFound",
              new String[]{collectionId},
              request.getLocale()));
    }

    try {
      String collectionFilter = collectionService.retrieveCollectionFilter(source);
      String query = recordsEsQueryBuilder.buildQuerySingleRecord(recordId, collectionFilter, null);

      String queryResponse = proxy.searchAndGetResult(request.getSession(), request, query, null);

      Document queryResult = XmlUtil.parseXmlString(queryResponse);
      String total = queryResult.getChildNodes().item(0).getAttributes().getNamedItem("total")
          .getNodeValue();

      if (Integer.parseInt(total) == 0) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            messages.getMessage("ogcapir.exception.collectionItem.notFound",
                new String[]{recordId, collectionId},
                ((HttpServletRequest) nativeWebRequest.getNativeRequest()).getLocale()));
      }

      Node metadataResult = queryResult.getChildNodes().item(0).getFirstChild();

      streamResult(response, XmlUtil.getNodeString(metadataResult),
          MediaType.APPLICATION_XML_VALUE);

      return ResponseEntity.ok().build();
    } catch (Exception ex) {
      // TODO: Log exception
      throw new RuntimeException(ex);
    }
  }


  /**
   * Collection item as HTML.
   */
  @GetMapping(
      value = "/collections/{collectionId}/items/{recordId}",
      produces = {
          MediaType.TEXT_HTML_VALUE,
          MediaType.ALL_VALUE
      })
  public String collectionsCollectionIdItemsRecordIdGetAsHtml(
      @ApiParam(value = "Identifier (name) of a specific collection", required = true)
      @PathVariable("collectionId")
          String collectionId,
      @ApiParam(value = "Identifier (name) of a specific record", required = true)
      @PathVariable("recordId")
          String recordId, HttpServletRequest request,
      Model model) {
    Locale locale = LocaleContextHolder.getLocale();
    String language = locale.getISO3Language();
    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find collection");
    }

    try {
      JsonNode recordAsJson = getRecordAsJson(collectionId, recordId, request, source, "json");

      Metadata record = metadataRepository.findOneByUuid(recordId);
      if (record == null) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
            messages.getMessage("ogcapir.exception.collectionItem.notFound",
                new String[]{recordId, collectionId},
                ((HttpServletRequest) nativeWebRequest.getNativeRequest()).getLocale()));
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
          new Item(recordId, null, record.getData())
      ));

      model.addAttribute("source", modelSource.toSource());
      viewUtility.addi18n(model, locale, List.of(record.getDataInfo().getSchemaId()), request);
      return "ogcapir/item";
    } catch (Exception ex) {
      // TODO: Log exception
      throw new RuntimeException(ex);
    }
  }


  /**
   * Collection items as XML.
   *
   * <p>In RSS, By default RSS feed is sorted by record
   * change date unless you defined a custom sort parameter.
   */
  @GetMapping(value = "/collections/{collectionId}/items",
      produces = {
          MediaType.APPLICATION_JSON_VALUE,
          GnMediaType.APPLICATION_JSON_LD_VALUE,
          MediaType.APPLICATION_RSS_XML_VALUE})
  @Override
  // TODO: support datetime, type, q, externalids
  public ResponseEntity<Void> collectionsCollectionIdItemsGet(
      String collectionId,
      List<BigDecimal> bbox,
      String datetime,
      Integer limit,
      Integer startindex,
      String type,
      List<String> q,
      List<String> externalids,
      List<String> sortby) {

    HttpServletRequest request = ((HttpServletRequest) nativeWebRequest.getNativeRequest());
    HttpServletResponse response = ((HttpServletResponse) nativeWebRequest.getNativeResponse());

    sortby = setDefaultRssSortBy(sortby, request);

    String queryResponse = search(collectionId, bbox, datetime, limit, startindex, type, q,
        externalids, sortby);

    try {
      streamResult(response, queryResponse, getResponseContentType(request));
    } catch (IOException ioException) {
      throw new RuntimeException(ioException);
    }

    return ResponseEntity.ok().build();
  }

  private List<String> setDefaultRssSortBy(List<String> sortby, HttpServletRequest request) {
    if ("rss".equals(request.getParameter("f"))
        && (sortby == null || sortby.size() == 0)) {
      sortby = new ArrayList<>();
      sortby.add(String.format("%s:%s",
          IndexRecordFieldNames.dateStamp, "desc"));
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
      List<String> sortby) {

    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find collection");
    }

    HttpServletRequest request = ((HttpServletRequest) nativeWebRequest.getNativeRequest());

    String collectionFilter = collectionService.retrieveCollectionFilter(source);
    String query = recordsEsQueryBuilder
        .buildQuery(q, externalids, bbox, startindex, limit, collectionFilter, sortby);
    try {
      return proxy.searchAndGetResult(request.getSession(), request, query, null);
    } catch (Exception ex) {
      // TODO: Log exception
      throw new RuntimeException(ex);
    }
  }


  /**
   * Collection items as XML.
   */
  @GetMapping(value = "/collections/{collectionId}/items",
      produces = {
          MediaType.APPLICATION_XML_VALUE
      })
  public ResponseEntity<Void> collectionsCollectionIdItemsGetAsXml(
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
          List<String> sortby) {

    return collectionsCollectionIdItemsGet(
        collectionId, bbox, datetime, limit, startindex, type, q, externalids, sortby);
  }


  /**
   * Collection items as HTML.
   */
  @GetMapping(value = "/collections/{collectionId}/items",
      produces = {
          MediaType.TEXT_HTML_VALUE,
          MediaType.ALL_VALUE
      })
  public String collectionsCollectionIdItemsGetAsHtml(
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
      Model model,
      HttpServletRequest request) {
    Locale locale = LocaleContextHolder.getLocale();
    String language = locale.getISO3Language();
    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find collection");
    }

    String collectionFilter = collectionService.retrieveCollectionFilter(source);
    String query = recordsEsQueryBuilder
        .buildQuery(q, externalids, bbox, startindex, limit, collectionFilter, sortby);
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
    return "ogcapir/collection";
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
