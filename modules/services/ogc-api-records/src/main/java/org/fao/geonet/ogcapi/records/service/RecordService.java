/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.service;

import static org.fao.geonet.ogcapi.records.controller.ItemApiController.EXCEPTION_COLLECTION_ITEM_NOT_FOUND;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.common.search.ElasticSearchProxy;
import org.fao.geonet.domain.Setting;
import org.fao.geonet.domain.Source;
import org.fao.geonet.domain.SourceType;
import org.fao.geonet.ogcapi.records.util.RecordsEsQueryBuilder;
import org.fao.geonet.repository.SettingRepository;
import org.fao.geonet.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;


@Service
@Slf4j(topic = "org.fao.geonet.ogcapi.records")
public class RecordService {

  @Autowired
  CollectionService collectionService;

  @Autowired
  RecordsEsQueryBuilder recordsEsQueryBuilder;

  @Autowired
  ElasticSearchProxy proxy;

  @Autowired
  MessageSource messages;


  @Autowired
  SettingRepository settingRepository;

  @Autowired
  SourceRepository sourceRepository;


  /**
   * For a source (sub-portal), get the ServiceRecord's uuid.
   * 1. usually, its part of the source object (source#getServiceRecord())
   * 2. however, if its the full-portal, there is no GUI to edit this.  In this case
   *    we use the CSW setting (system/csw/capabilityRecordUuid).
   * @param source which sub-portal?
   * @return uuid of the linked record or null
   */
  public String getLinkedRecordUuid(Source source) {
    if (source == null) {
      return null;
    }

    var uuid = source.getServiceRecord();
    if (StringUtils.isEmpty(uuid) || uuid.trim().equals("-1")) {
      if (source.getType().equals(SourceType.portal)) {
        // this is the main portal - we have a 2nd way to get the link (admin->settings->CSW)
        Setting setting = settingRepository.getOne("system/csw/capabilityRecordUuid");
        uuid = setting.getStoredValue();
      }
    }
    if (StringUtils.isEmpty(uuid) || uuid.trim().equals("-1")) {
      return null;
    }
    return uuid;
  }


  /**
   * Gets the main GN portal (from GN DB table "sources").
   *
   * @return Gets the main GN portal (from GN DB table "sources")
   */
  public Source getMainPortal() {
    return sourceRepository.findByType(SourceType.portal, null).get(0);
  }


  /**
   * Given a source, get the parsed Elastic Index JSON.
   *
   * @param request user request (for security)
   * @param source  which sub-portal to get the linked serviceRecord
   * @return parsed Elastic Index JSON for the source's linked serviceRecord
   */
  public Map<String, Object> getLinkedServiceRecord(HttpServletRequest request,
      Source source) {

    var mainPortal = getMainPortal();

    var uuid = getLinkedRecordUuid(source);

    if (StringUtils.isEmpty(uuid) || uuid.trim().equals("-1")) {
      return null;
    }
    try {
      var info = getRecordAsJson(mainPortal.getUuid(),
          uuid, request, mainPortal,
          "json")
          .get("_source");
      ObjectMapper mapper = new ObjectMapper();
      Map<String, Object> result = mapper.convertValue(info,
          new TypeReference<Map<String, Object>>() {
          });
      return result;
    } catch (Exception ex) {
      // mislinked record?  not published? error processing the record?
      log.error(String.format(
          "An error occurred while trying to retrieve the linked ServiceRecord for sub-portal "
              + "'%s' (link uuid '%s'). Error is: %s",
          source.getUuid(), uuid, ex.getMessage()));
    }
    return null;
  }


  /**
   * Get the Elastic Index JSON.
   *
   * @param collectionId which collection is the record apart of
   * @param recordId     which uuid to get
   * @param request      incomming user request (for security)
   * @param source       from the GN DB "source" table for this collectionId
   * @param type         what type of record
   * @return parsed as a JSON object
   * @throws Exception if there was a problem retrieving the record
   */
  public JsonNode getRecordAsJson(
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

}
