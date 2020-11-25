/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.fao.geonet.common.search.ElasticSearchProxy;
import org.fao.geonet.domain.Source;
import org.fao.geonet.ogcapi.records.model.XsltModel;
import org.fao.geonet.ogcapi.records.service.CollectionService;
import org.fao.geonet.ogcapi.records.util.RecordsEsQueryBuilder;
import org.fao.geonet.ogcapi.records.util.XmlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@Controller
public class RecordApiController implements RecordApi {

  @Autowired
  private CollectionService collectionService;

  @Autowired
  ElasticSearchProxy proxy;

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
  public ResponseEntity<Void> collectionsCollectionIdItemsRecordIdGet(String collectionId,
      String recordId) {

    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find collection");
    }

    HttpServletRequest request = ((HttpServletRequest) nativeWebRequest.getNativeRequest());
    HttpServletResponse response = ((HttpServletResponse) nativeWebRequest.getNativeResponse());

    try {
      String collectionFilter = collectionService.retrieveCollectionFilter(source);
      String query = RecordsEsQueryBuilder.buildQuerySingleRecord(recordId, collectionFilter, null);

      String queryResponse = proxy.searchAndGetResult(request.getSession(), request, query, null);

      ObjectMapper mapper = new ObjectMapper();
      JsonFactory factory = mapper.getFactory();
      JsonParser parser = factory.createParser(queryResponse);
      JsonNode actualObj = mapper.readTree(parser);

      JsonNode totalValue = actualObj.get("hits").get("total").get("value");

      if ((totalValue == null) || (totalValue.intValue() == 0)) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find item");
      }

      JsonNode recordValue = actualObj.get("hits").get("hits").get(0);

      streamResult(response, recordValue.toPrettyString(), MediaType.APPLICATION_JSON_VALUE);

      return ResponseEntity.ok().build();
    } catch (Exception ex) {
      // TODO: Log exception
      throw new RuntimeException(ex);
    }

  }


  /**
   * Collection item as XML.
   */
  @GetMapping(value = "/collections/{collectionId}/items/{recordId}",
      produces = {"application/xml"})
  public ResponseEntity<Void> collectionsCollectionIdItemsRecordIdGetAsXml(
      @ApiParam(value = "Identifier (name) of a specific collection", required = true)
      @PathVariable("collectionId")
          String collectionId,
      @ApiParam(value = "Identifier (name) of a specific record", required = true)
      @PathVariable("recordId")
          String recordId) {

    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find collection");
    }

    HttpServletRequest request = ((HttpServletRequest) nativeWebRequest.getNativeRequest());
    HttpServletResponse response = ((HttpServletResponse) nativeWebRequest.getNativeResponse());

    try {
      String collectionFilter = collectionService.retrieveCollectionFilter(source);
      String query = RecordsEsQueryBuilder.buildQuerySingleRecord(recordId, collectionFilter, null);

      String queryResponse = proxy.searchAndGetResult(request.getSession(), request, query, null);

      Document queryResult = XmlUtil.parseXmlString(queryResponse);
      String total = queryResult.getChildNodes().item(0).getAttributes().getNamedItem("total")
          .getNodeValue();

      if (Integer.parseInt(total) == 0) {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find item");
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

    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find collection");
    }

    HttpServletRequest request = ((HttpServletRequest) nativeWebRequest.getNativeRequest());
    HttpServletResponse response = ((HttpServletResponse) nativeWebRequest.getNativeResponse());

    try {
      String collectionFilter = collectionService.retrieveCollectionFilter(source);
      String query = RecordsEsQueryBuilder
          .buildQuery(bbox, startindex, limit, collectionFilter, sortby);

      String queryResponse = proxy.searchAndGetResult(request.getSession(), request, query, null);

      streamResult(response, queryResponse, MediaType.APPLICATION_JSON_VALUE);

      return ResponseEntity.ok().build();
    } catch (Exception ex) {
      // TODO: Log exception
      throw new RuntimeException(ex);
    }
  }


  /**
   * Collection items as XML.
   */
  @GetMapping(value = "/collections/{collectionId}/items",
      produces = {"application/xml"})
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
      @ApiParam(value = "", defaultValue = "9")
      @RequestParam(value = "startindex", required = false, defaultValue = "9")
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
      produces = {"text/html"})
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
      @ApiParam(value = "", defaultValue = "9")
      @RequestParam(value = "startindex", required = false, defaultValue = "9")
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
      Model model) {
    Locale locale = LocaleContextHolder.getLocale();
    String language = locale.getISO3Language();
    Source source = collectionService.retrieveSourceForCollection(collectionId);

    XsltModel modelSource = new XsltModel();
    modelSource.setCollection(source);
    model.addAttribute("source", modelSource.toSource());
    model.addAttribute("language", language);
    return "ogcapir/collection";
  }


  /**
   * Streams the content body in the response stream using the content-type provided.
   */
  private void streamResult(HttpServletResponse response, String content,
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
}
