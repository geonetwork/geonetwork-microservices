package org.fao.geonet.ogcapi.records.util;

import static org.fao.geonet.ogcapi.records.util.JsonUtils.getLangString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.ogcapi.records.controller.model.CollectionInfo;
import org.fao.geonet.ogcapi.records.model.OgcApiContact;
import org.fao.geonet.ogcapi.records.model.OgcApiExtent;
import org.fao.geonet.ogcapi.records.model.OgcApiLanguage;
import org.fao.geonet.ogcapi.records.model.OgcApiSpatialExtent;
import org.fao.geonet.ogcapi.records.model.OgcApiTemporalExtent;
import org.fao.geonet.ogcapi.records.model.OgcApiTheme;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@Slf4j(topic = "org.fao.geonet.ogcapi.records.util")
public class ElasticIndexJson2CollectionInfo {

  /**
   * inject the "extra" info from the LinkedServiceRecord into the CollectionInfo.
   *
   * @param collectionInfo      collection metadata we've gathered so far (usually not much)
   * @param linkedServiceRecord JSON of the linked Service record (GN's DB "source"
   *                            "serviceRecord")
   */
  public void injectLinkedServiceRecordInfo(CollectionInfo collectionInfo,
      Map<String, Object> linkedServiceRecord) {
    if (linkedServiceRecord == null) {
      return;
    }

    handleTitle(collectionInfo, linkedServiceRecord);
    handleDescription(collectionInfo, linkedServiceRecord);

    handleContacts(collectionInfo, linkedServiceRecord);

    OgcApiSpatialExtent spatialExtent = handleSpatialExtent(linkedServiceRecord);
    OgcApiTemporalExtent temporalExtent = handleTemporalExtent(linkedServiceRecord);
    handleExtent(collectionInfo, spatialExtent, temporalExtent);

    handleCrs(collectionInfo, linkedServiceRecord);

    handleCreateDate(collectionInfo, linkedServiceRecord);
    handleChangeDate(collectionInfo, linkedServiceRecord);

    handleTags(collectionInfo, linkedServiceRecord);

    handleLanguage(collectionInfo, linkedServiceRecord);
    handleOtherLangs(collectionInfo, linkedServiceRecord);

    handleThemes(collectionInfo, linkedServiceRecord);

    handleLicenses(collectionInfo, linkedServiceRecord);
  }


  //its a bit unclear what to do here - there's a big difference between MD record,
  // Elastic Index JSON versus what's expected in the ogcapi license field.  We do the simple
  // action
  private void handleLicenses(CollectionInfo collectionInfo,
      Map<String, Object> linkedServiceRecord) {
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

  //process allKeywords to get themes
  private void handleThemes(CollectionInfo collectionInfo,
      Map<String, Object> linkedServiceRecord) {
    var myThemeKeywords = linkedServiceRecord.get("allKeywords");
    collectionInfo.setThemes(OgcApiTheme.parseElasticIndex((Map<String, Object>) myThemeKeywords));
  }

  //process otherLanguage to get the other languages
  private void handleOtherLangs(CollectionInfo collectionInfo,
      Map<String, Object> linkedServiceRecord) {
    var myOtherLangs = linkedServiceRecord.get("otherLanguage");
    if (myOtherLangs != null && myOtherLangs instanceof List) {
      var langs = (List) myOtherLangs;
      collectionInfo.setLanguages(new ArrayList<>());
      for (var lang : langs) {
        collectionInfo.getLanguages().add(new OgcApiLanguage(lang.toString()));
      }
    }
  }

  //process main language
  private void handleLanguage(CollectionInfo collectionInfo,
      Map<String, Object> linkedServiceRecord) {
    var myMainLang = linkedServiceRecord.get("mainLanguage");
    if (myMainLang != null && StringUtils.hasText(myMainLang.toString())) {
      collectionInfo.setLanguage(new OgcApiLanguage(myMainLang.toString()));
    }
  }

  //handle the "tags" to create keywords
  private void handleTags(CollectionInfo collectionInfo, Map<String, Object> linkedServiceRecord) {
    var myTags = linkedServiceRecord.get("tag");
    if (myTags != null && myTags instanceof List) {
      var tags = (List) myTags;
      collectionInfo.setKeywords(new ArrayList<>());
      for (var tag : tags) {
        collectionInfo.getKeywords().add(getLangString(tag));
      }
    }
  }

  //process the change date
  private void handleChangeDate(CollectionInfo collectionInfo,
      Map<String, Object> linkedServiceRecord) {
    var updateDate = linkedServiceRecord.get("changeDate");
    if (updateDate != null && StringUtils.hasText(updateDate.toString())) {
      collectionInfo.setUpdated(updateDate.toString());
    }
  }

  //process the creation date
  private void handleCreateDate(CollectionInfo collectionInfo,
      Map<String, Object> linkedServiceRecord) {
    var createDate = linkedServiceRecord.get("createDate");
    if (createDate != null && StringUtils.hasText(createDate.toString())) {
      collectionInfo.setCreated(createDate.toString());
    }
  }

  //process CRS
  private void handleCrs(CollectionInfo collectionInfo, Map<String, Object> linkedServiceRecord) {
    var myCrss = linkedServiceRecord.get("coordinateSystem");
    if (myCrss != null && myCrss instanceof List) {
      var crss = (List) myCrss;
      collectionInfo.setCrs(new ArrayList<>());
      for (var crs : crss) {
        collectionInfo.getCrs().add(crs.toString());
      }
    }
  }

  //combine spatial and temporal extent
  private void handleExtent(CollectionInfo collectionInfo, OgcApiSpatialExtent spatialExtent,
      OgcApiTemporalExtent temporalExtent) {
    OgcApiExtent extent = new OgcApiExtent(spatialExtent, temporalExtent);
    if (spatialExtent != null || temporalExtent != null) {
      collectionInfo.setExtent(extent);
    }
  }

  //process Temporal Extent
  private OgcApiTemporalExtent handleTemporalExtent(Map<String, Object> linkedServiceRecord) {
    OgcApiTemporalExtent temporalExtent = null;
    var myTemporalExtent = linkedServiceRecord.get("resourceTemporalDateRange");
    if (myTemporalExtent != null && myTemporalExtent instanceof List) {
      myTemporalExtent = ((List) myTemporalExtent).get(0);
      temporalExtent = OgcApiTemporalExtent.fromGnIndexRecord(
          (Map<String, Object>) myTemporalExtent);
    }
    return temporalExtent;
  }

  //process the spatial extent
  private OgcApiSpatialExtent handleSpatialExtent(Map<String, Object> linkedServiceRecord) {
    OgcApiSpatialExtent spatialExtent = null;
    var mySpatialExtent = linkedServiceRecord.get("geom");
    if (mySpatialExtent != null
        && mySpatialExtent instanceof List) {
      mySpatialExtent = ((List) mySpatialExtent).get(0);
      spatialExtent = OgcApiSpatialExtent.fromGnIndexRecord((Map<String, Object>) mySpatialExtent);
    }
    return spatialExtent;
  }

  //process contracts
  private void handleContacts(CollectionInfo collectionInfo,
      Map<String, Object> linkedServiceRecord) {
    var contacts = linkedServiceRecord.get("contact");
    if (contacts != null && linkedServiceRecord.get("contact") instanceof List) {
      var cs = (List) contacts;
      collectionInfo.setContacts(new ArrayList<>());
      for (var contactMap : cs) {
        var contact = OgcApiContact.fromIndexMap((Map<String, Object>) contactMap);
        collectionInfo.getContacts().add(contact);
      }
    }
  }

  //override description (abstract) from attached ServiceRecord
  private void handleDescription(CollectionInfo collectionInfo,
      Map<String, Object> linkedServiceRecord) {
    var desc = getLangString(linkedServiceRecord.get("resourceAbstractObject"));
    if (desc != null) {
      collectionInfo.setDescription(desc);
    }
  }


  //override title from attached ServiceRecord
  private void handleTitle(CollectionInfo collectionInfo, Map<String, Object> linkedServiceRecord) {
    var title = getLangString(linkedServiceRecord.get("resourceTitleObject"));
    if (title != null) {
      collectionInfo.setTitle(title);
    }
  }
}
