/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.util;


import static java.util.Arrays.asList;
import static org.fao.geonet.ogcapi.records.util.LinksItemsBuilder.getHref;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.common.search.SearchConfiguration;
import org.fao.geonet.common.search.SearchConfiguration.Format;
import org.fao.geonet.domain.Setting;
import org.fao.geonet.domain.Source;
import org.fao.geonet.domain.SourceType;
import org.fao.geonet.ogcapi.records.controller.model.CollectionInfo;
import org.fao.geonet.ogcapi.records.controller.model.Extent.CrsEnum;
import org.fao.geonet.ogcapi.records.controller.model.Link;
import org.fao.geonet.ogcapi.records.model.OgcApiContact;
import org.fao.geonet.ogcapi.records.model.OgcApiExtent;
import org.fao.geonet.ogcapi.records.model.OgcApiLanguage;
import org.fao.geonet.ogcapi.records.model.OgcApiSpatialExtent;
import org.fao.geonet.ogcapi.records.model.OgcApiTemporalExtent;
import org.fao.geonet.ogcapi.records.model.OgcApiTheme;
import org.fao.geonet.ogcapi.records.service.RecordService;
import org.fao.geonet.repository.SettingRepository;
import org.fao.geonet.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j(topic = "org.fao.geonet.ogcapi.records")
public class CollectionInfoBuilder {

  @Autowired
  RecordService recordService;

  @Autowired
  private SourceRepository sourceRepository;

  @Autowired
  private SettingRepository settingRepository;

  public CollectionInfoBuilder() {

  }

  /**
   * todo - be language aware (send in desired language).  Move to utility class.
   *
   * @param jsonNode json node for the potentially multi-language string
   * @return "correct" language value.
   */
  public static String getLangString(Object jsonNode) {
    if (jsonNode == null) {
      return null;
    }
    if ((jsonNode instanceof String)) {
      return (String) jsonNode;
    }
    if (jsonNode instanceof Map) {
      var map = (Map<String, Object>) jsonNode;
      if (map.containsKey("default")) {
        return map.get("default").toString();
      }
      return null;
    }
    return null;
  }

  /**
   *  Simple utility class to get a JSON value as a string.
   * @param o json object
   * @return null or o.toString()
   */
  public static String getAsString(Object o) {
    if (o == null) {
      return null;
    }
    var result = o.toString();
    return result;
  }

  /**
   * Gets the main GN portal (from GN DB table "sources").
   * @return Gets the main GN portal (from GN DB table "sources")
   */
  public Source getMainPortal() {
    return sourceRepository.findByType(SourceType.portal, null).get(0);
  }

  /**
   * Given a source, get the parsed Elastic Index JSON.
   *
   * @param request user request (for security)
   * @param source which sub-portal to get the linked serviceRecord
   * @return parsed Elastic Index JSON for the source's linked serviceRecord
   */
  public Map<String, Object> getLinkedServiceRecord(HttpServletRequest request,
      Source source) {

    var mainPortal = getMainPortal();
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
    try {
      var info = recordService.getRecordAsJson(mainPortal.getUuid(),
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
   * Build Collection info from source table.
   *
   * @param source    from the GN DB Table "sources"
   * @param language which language is the request wanting
   * @param baseUrl  base url for the ogcapi
   * @param format what format are the results requested in
   * @param configuration config for searching
   * @param request user request (for security)
   * @return CollectionInfo filled in from GN DB Table "sources" and the Elastic Index JSON
   */
  public CollectionInfo buildFromSource(Source source,
      String language,
      String baseUrl,
      Optional<Format> format,
      SearchConfiguration configuration,
      HttpServletRequest request) {
    String name;

    if (source.getType() == SourceType.portal) {
      name = "main";
    } else {
      name = source.getUuid();
    }

    CollectionInfo collectionInfo = new CollectionInfo();

    collectionInfo.setId(name);
    String label = source.getLabel(language);
    // The source label may contain a description
    // eg. "INSPIRE|Data sets and services for the environment"
    String[] titleAndDescription = label.split("\\|");
    collectionInfo.setDescription(
        titleAndDescription.length > 1 ? titleAndDescription[1] : "");
    collectionInfo.setTitle(
        titleAndDescription.length > 1 ? titleAndDescription[0] : label);
    collectionInfo
        .setCrs(asList(CrsEnum.HTTP_WWW_OPENGIS_NET_DEF_CRS_OGC_1_3_CRS84.getValue()));

    var spatialExtent = new OgcApiSpatialExtent();
    spatialExtent.crs = OgcApiSpatialExtent.CRS84;
    spatialExtent.setBbox(asList(-180.0, -90.0, 180.0, 90.0));
    var extent = new OgcApiExtent(spatialExtent, null);
    collectionInfo.setExtent(extent);

    // TODO: Accept format parameter.
    baseUrl = baseUrl + (!baseUrl.endsWith("/") ? "/" : "");
    URI collectionUri = URI.create(baseUrl).resolve(name);
    Link currentDoc = new Link();
    currentDoc.setRel("self");
    currentDoc.setHref(getHref(collectionUri.toString(), format));
    currentDoc.setType(format.get().getMimeType());
    currentDoc.setHreflang(language);

    List<Link> linkList = LinksItemsBuilder.build(
        format, collectionUri.toString(), language, configuration);
    linkList.forEach(collectionInfo::addLinksItem);

    var linkedServiceRecord = getLinkedServiceRecord(request, source);
    injectLinkedServiceRecordInfo(collectionInfo, linkedServiceRecord);

    return collectionInfo;
  }

  /**
   * inject the "extra" info from the LinkedServiceRecord into the CollectionInfo.
   *
   * @param collectionInfo collection metadata we've gathered so far (usually not much)
   * @param linkedServiceRecord JSON of the linked Service record (GN's DB "source" "serviceRecord")
   */
  private void injectLinkedServiceRecordInfo(CollectionInfo collectionInfo,
      Map<String, Object> linkedServiceRecord) {
    if (linkedServiceRecord == null) {
      return;
    }

    //override title from attached ServiceRecord
    var title = getLangString(linkedServiceRecord.get("resourceTitleObject"));
    if (title != null) {
      collectionInfo.setTitle(title);
    }

    //override description (abstract) from attached ServiceRecord
    var desc = getLangString(linkedServiceRecord.get("resourceAbstractObject"));
    if (desc != null) {
      collectionInfo.setDescription(desc);
    }

    var contacts = linkedServiceRecord.get("contact");
    if (contacts != null && linkedServiceRecord.get("contact") instanceof List) {
      var cs = (List) contacts;
      collectionInfo.setContacts(new ArrayList<>());
      for (var contactMap : cs) {
        var contact = OgcApiContact.fromIndexMap((Map<String, Object>) contactMap);
        collectionInfo.getContacts().add(contact);
      }
    }

    OgcApiSpatialExtent spatialExtent = null;
    var mySpatialExtent = linkedServiceRecord.get("geom");
    if (mySpatialExtent != null
        && mySpatialExtent instanceof List) {
      mySpatialExtent = ((List) mySpatialExtent).get(0);
      spatialExtent = OgcApiSpatialExtent.fromGnIndexRecord((Map<String, Object>) mySpatialExtent);
    }

    OgcApiTemporalExtent temporalExtent = null;
    var myTemporalExtent = linkedServiceRecord.get("resourceTemporalDateRange");
    if (myTemporalExtent != null && myTemporalExtent instanceof List) {
      myTemporalExtent = ((List) myTemporalExtent).get(0);
      temporalExtent = OgcApiTemporalExtent.fromGnIndexRecord(
          (Map<String, Object>) myTemporalExtent);
    }

    OgcApiExtent extent = new OgcApiExtent(spatialExtent, temporalExtent);
    if (spatialExtent != null || temporalExtent != null) {
      collectionInfo.setExtent(extent);
    }

    var myCrss = linkedServiceRecord.get("coordinateSystem");
    if (myCrss != null && myCrss instanceof List) {
      var crss = (List) myCrss;
      collectionInfo.setCrs(new ArrayList<>());
      for (var crs : crss) {
        collectionInfo.getCrs().add(crs.toString());
      }
    }

    var createDate = linkedServiceRecord.get("createDate");
    if (createDate != null && StringUtils.hasText(createDate.toString())) {
      collectionInfo.setCreated(createDate.toString());
    }

    var updateDate = linkedServiceRecord.get("changeDate");
    if (updateDate != null && StringUtils.hasText(updateDate.toString())) {
      collectionInfo.setUpdated(updateDate.toString());
    }

    var myTags = linkedServiceRecord.get("tag");
    if (myTags != null && myTags instanceof List) {
      var tags = (List) myTags;
      collectionInfo.setKeywords(new ArrayList<>());
      for (var tag : tags) {
        collectionInfo.getKeywords().add(getLangString(tag));
      }
    }

    //index record doesn't contain "otherlanguages"
    var myMainLang = linkedServiceRecord.get("mainLanguage");
    if (myMainLang != null && StringUtils.hasText(myMainLang.toString())) {
      collectionInfo.setLanguage(new OgcApiLanguage(myMainLang.toString()));
    }

    var myOtherLangs = linkedServiceRecord.get("otherLanguage");
    if (myOtherLangs != null && myOtherLangs instanceof List) {
      var langs = (List) myOtherLangs;
      collectionInfo.setLanguages(new ArrayList<>());
      for (var lang : langs) {
        collectionInfo.getLanguages().add(new OgcApiLanguage(lang.toString()));
      }
    }

    var myThemeKeywords = linkedServiceRecord.get("allKeywords");
    collectionInfo.setThemes(OgcApiTheme.parseElasticIndex((Map<String, Object>) myThemeKeywords));

    //its a bit unclear what to do here - there's a big difference between MD record,
    // Elastic Index JSON versus what's expected in the ogcapi license field.  We do the simple
    // action
    var myLicense = linkedServiceRecord.get("MD_LegalConstraintsUseLimitationObject");
    if (myLicense != null && myLicense instanceof List) {
      var myLiceseList = (List) myLicense;
      if (myLiceseList.size() > 0) {
        var license = getLangString(myLiceseList.get(0));
        collectionInfo.setLicense(license);
      }
      if (myLiceseList.size() > 1) {
        var license2 = getLangString(myLiceseList.get(1));
        collectionInfo.setRights(license2);
      }
    }


  }

}
