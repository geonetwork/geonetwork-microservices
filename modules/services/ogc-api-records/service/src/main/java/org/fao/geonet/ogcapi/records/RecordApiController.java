/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.io.IOUtils;
import org.fao.geonet.common.search.ElasticSearchProxy;
import org.fao.geonet.common.xml.XmlList;
import org.fao.geonet.domain.Source;
import org.fao.geonet.ogcapi.records.rest.ogc.model.CollectionInfo;
import org.fao.geonet.ogcapi.records.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class RecordApiController implements RecordApi {
  @Autowired
  private CollectionService collectionService;

  @Autowired
  ElasticSearchProxy proxy;

  /**
   * Only to support sample responses from {@link RecordApi}, remove once
   * all its methods are implemented.
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
      String query =  buildQueryRecord(recordId, collectionFilter);

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

      PrintWriter out = response.getWriter();
      try {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(recordValue.toPrettyString());
      } finally {
        out.flush();
      }

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
      String query = buildQuery(bbox, startindex, limit, collectionFilter, sortby);

      String queryResponse = proxy.searchAndGetResult(request.getSession(), request, query, null);

      PrintWriter out = response.getWriter();
      try {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(queryResponse);
      } finally {
        out.flush();
      }

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
      @PathVariable
      String collectionId,
      @RequestParam(required = false)
      List<BigDecimal> bbox,
      @RequestParam(required = false)
      String datetime,
      @RequestParam(required = false)
      Integer limit,
      @RequestParam(required = false)
      Integer startindex,
      @RequestParam(required = false)
      String type,
      @RequestParam(required = false)
      List<String> q,
      @RequestParam(required = false)
      List<String> externalids,
      @RequestParam(required = false)
      List<String> sortby) {
    return collectionsCollectionIdItemsGet(collectionId, bbox, datetime, limit,
        startindex, type, q, externalids, sortby);
  }

  /**
   * Collection items as HTML.
   */
  @GetMapping(value = "/collections/{collectionId}/items",
      produces = {"text/html"})
  public String collectionsCollectionIdItemsGetAsHtml(
      @PathVariable
          String collectionId,
      @RequestParam(required = false)
          List<BigDecimal> bbox,
      @RequestParam(required = false)
          String datetime,
      @RequestParam(required = false)
          Integer limit,
      @RequestParam(required = false)
          Integer startindex,
      @RequestParam(required = false)
          String type,
      @RequestParam(required = false)
          List<String> q,
      @RequestParam(required = false)
          List<String> externalids,
      @RequestParam(required = false)
          List<String> sortby,
      Model model) {
    Locale locale = LocaleContextHolder.getLocale();
    String language = locale.getISO3Language();
    Source source = collectionService.retrieveSourceForCollection(collectionId);
    StringWriter sw = new StringWriter();
    try {
      JAXBContext context = JAXBContext.newInstance(XmlList.class, Source.class);
      Marshaller marshaller = context.createMarshaller();
      marshaller.marshal(new XmlList<>(Arrays.asList(source)), sw);
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    model.addAttribute("source", IOUtils.toInputStream(
        String.format(
            "<model><collection>%s</collection><items>%s<items></model>",
            sw.toString(), "")));
    model.addAttribute("language", language);
    return "ogcapir/items";
  }



  private String buildQueryRecord(String recordId, String collectionFilter) {
    return String.format("{\"from\": %d, \"size\": %d, "
            + "\"query\": {\"query_string\": "
            + "{\"query\": \"+_id:%s %s +isTemplate:n\"}}}",
        0, 1, recordId, collectionFilter);
  }

  private String buildQuery(List<BigDecimal> bbox, Integer startIndex, Integer limit,
      String collectionFilter, List<String> sortBy) {
    String geoFilter = "";

    if (bbox != null) {
      geoFilter = String.format(", {\"geo_shape\": {\"geom\": {\n"
          + "                \"shape\": {\n"
          + "                    \"type\": \"envelope\",\n"
          + "                    \"coordinates\": [\n"
          + "                        [\n"
          + "                            %f,\n"
          + "                            %f\n"
          + "                        ],\n"
          + "                        [\n"
          + "                            %f,\n"
          + "                            %f\n"
          + "                        ]\n"
          + "                    ]\n"
          + "                },\n"
          + "                \"relation\": \"intersects\"\n"
          + "            }}}", bbox.get(0), bbox.get(1), bbox.get(2), bbox.get(3));
    }

    String sortByValue = "\"_score\"";

    if (sortBy != null) {
      List<String> sortByList = new ArrayList<>();
      sortBy.forEach(s -> {
        String[] sortByTokens = s.split(":");

        if (sortByTokens.length == 2) {
          sortByList.add(String.format("{\"%s\": \"%s\"}", sortByTokens[0], sortByTokens[1]));
        }
      });

      sortByValue = String.join(",", sortByList);
    }

    return String.format("{\"from\": %d, \"size\": %d, "
            + "\"sort\": [%s],"
            + "\"query\": {\"query_string\": "
            + "{\"query\": \"%s +isTemplate:n\"}} %s} ",
        startIndex, limit, sortByValue, collectionFilter, geoFilter);
  }
}
