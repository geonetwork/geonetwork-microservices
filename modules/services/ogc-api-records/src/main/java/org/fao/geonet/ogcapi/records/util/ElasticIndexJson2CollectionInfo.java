package org.fao.geonet.ogcapi.records.util;

import static org.fao.geonet.ogcapi.records.util.JsonUtils.getLangString;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.type.TypeFactory;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.ogcapi.records.controller.model.CollectionInfo;
import org.fao.geonet.ogcapi.records.model.OgcApiContact;
import org.fao.geonet.ogcapi.records.model.OgcApiExtent;
import org.fao.geonet.ogcapi.records.model.OgcApiLanguage;
import org.fao.geonet.ogcapi.records.model.OgcApiSpatialExtent;
import org.fao.geonet.ogcapi.records.model.OgcApiTemporalExtent;
import org.fao.geonet.ogcapi.records.model.OgcApiTheme;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


/**
 * Takes an  Elastic Index Json and injects it inside a collectioninfo.
 */
@Service
@Slf4j(topic = "org.fao.geonet.ogcapi.records.util")
public class ElasticIndexJson2CollectionInfo {


  /**
   * inject the "extra" info from the LinkedServiceRecord into the CollectionInfo.
   *
   * @param collectionInfo collection metadata we've gathered so far (usually not much)
   * @param linkedServiceRecord   Parsed JSON map of the linked Service record (GN's DB "source"
   *                       "serviceRecord")
   */
  public void injectLinkedServiceRecordInfo(CollectionInfo collectionInfo,
      Map<String, Object> linkedServiceRecord) {
    if (linkedServiceRecord == null) {
      return;
    }
    ObjectMapper objectMapper = org.fao.geonet.index.JsonUtils.getObjectMapper();

    IndexRecord indexRecord = objectMapper.convertValue(
        linkedServiceRecord,
        IndexRecord.class);

    injectLinkedServiceRecordInfo(collectionInfo, indexRecord);
  }

  /**
   * inject the "extra" info from the LinkedServiceRecord into the CollectionInfo.
   *
   * @param collectionInfo collection metadata we've gathered so far (usually not much)
   * @param indexRecord    Parsed JSON of the linked Service record (GN's DB "source"
   *                       "serviceRecord")
   */
  public void injectLinkedServiceRecordInfo(CollectionInfo collectionInfo,
      IndexRecord indexRecord) {
    if (indexRecord == null) {
      return;
    }

    handleTitle(collectionInfo, indexRecord);
    handleDescription(collectionInfo, indexRecord);

    handleContacts(collectionInfo, indexRecord);

    OgcApiSpatialExtent spatialExtent = handleSpatialExtent(indexRecord);
    OgcApiTemporalExtent temporalExtent = handleTemporalExtent(indexRecord);
    handleExtent(collectionInfo, spatialExtent, temporalExtent);

    handleCrs(collectionInfo, indexRecord);

    handleCreateDate(collectionInfo, indexRecord);
    handleChangeDate(collectionInfo, indexRecord);

    handleTags(collectionInfo, indexRecord);

    handleLanguage(collectionInfo, indexRecord);
    handleOtherLangs(collectionInfo, indexRecord);

    handleThemes(collectionInfo, indexRecord);

    handleLicenses(collectionInfo, indexRecord);
  }


  //its a bit unclear what to do here - there's a big difference between MD record,
  // Elastic Index JSON versus what's expected in the ogcapi license field.  We do the simple
  // action
  private void handleLicenses(CollectionInfo collectionInfo,
      IndexRecord indexRecord) {
    var myLicense = indexRecord.getMdLegalConstraintsUseLimitationObject();
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
      IndexRecord indexRecord) {
    var myThemeKeywords = indexRecord.getAllKeywords();
    collectionInfo.setThemes(OgcApiTheme.parseElasticIndex(myThemeKeywords));
  }

  //process otherLanguage to get the other languages
  private void handleOtherLangs(CollectionInfo collectionInfo,
      IndexRecord indexRecord) {
    var myOtherLangs = indexRecord.getOtherLanguage();
    if (myOtherLangs != null) {
      var langs = myOtherLangs;
      collectionInfo.setLanguages(new ArrayList<>());
      for (var lang : langs) {
        collectionInfo.getLanguages().add(new OgcApiLanguage(lang));
      }
    }
  }

  //process main language
  private void handleLanguage(CollectionInfo collectionInfo,
      IndexRecord indexRecord) {
    var myMainLang = indexRecord.getMainLanguage();
    if (myMainLang != null && StringUtils.hasText(myMainLang)) {
      collectionInfo.setLanguage(new OgcApiLanguage(myMainLang));
    }
  }

  //handle the "tags" to create keywords
  private void handleTags(CollectionInfo collectionInfo, IndexRecord indexRecord) {
    var myTags = indexRecord.getTag();
    if (myTags != null) {
      var tags = myTags;
      collectionInfo.setKeywords(new ArrayList<>());
      for (var tag : tags) {
        collectionInfo.getKeywords().add(getLangString(tag));
      }
    }
  }

  //process the change date
  private void handleChangeDate(CollectionInfo collectionInfo,
      IndexRecord indexRecord) {
    var updateDate = indexRecord.getChangeDate();
    if (updateDate != null && StringUtils.hasText(updateDate)) {
      collectionInfo.setUpdated(updateDate);
    }
  }

  //process the creation date
  private void handleCreateDate(CollectionInfo collectionInfo,
      IndexRecord indexRecord) {
    var createDate = indexRecord.getCreateDate();
    if (createDate != null && StringUtils.hasText(createDate)) {
      collectionInfo.setCreated(createDate);
    }
  }

  //process CRS
  private void handleCrs(CollectionInfo collectionInfo, IndexRecord indexRecord) {
    var myCrss = indexRecord.getCoordinateSystem();
    if (myCrss != null) {
      var crss = myCrss;
      collectionInfo.setCrs(new ArrayList<>());
      for (var crs : crss) {
        collectionInfo.getCrs().add(crs);
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
  private OgcApiTemporalExtent handleTemporalExtent(IndexRecord indexRecord) {
    OgcApiTemporalExtent temporalExtent = null;
    var myTemporalExtent = indexRecord.getResourceTemporalExtentDateRange();
    if (myTemporalExtent != null || myTemporalExtent.isEmpty()) {
      myTemporalExtent = indexRecord.getResourceTemporalDateRange();
    }
    if (myTemporalExtent != null || myTemporalExtent.isEmpty()) {
      temporalExtent = OgcApiTemporalExtent.fromGnIndexRecord(myTemporalExtent.get(0));
    }
    return temporalExtent;
  }

  //process the spatial extent
  private OgcApiSpatialExtent handleSpatialExtent(IndexRecord indexRecord) {
    OgcApiSpatialExtent spatialExtent = null;
    var mySpatialExtent = indexRecord.getGeometries();
    if (mySpatialExtent != null && !mySpatialExtent.isEmpty()) {
      Map<String, Object> map = null;
      try {
        map = new org.codehaus.jackson.map.ObjectMapper().readValue(mySpatialExtent.get(0),
            TypeFactory.mapType(HashMap.class, String.class, Object.class));
      } catch (IOException e) {
        return null;
      }
      spatialExtent = OgcApiSpatialExtent.fromGnIndexRecord(map);
    }
    return spatialExtent;
  }

  //process contracts
  private void handleContacts(CollectionInfo collectionInfo,
      IndexRecord indexRecord) {
    var contacts = indexRecord.getContact();
    if (contacts != null && !contacts.isEmpty()) {
      collectionInfo.setContacts(new ArrayList<>());
      for (var contactInfo : contacts) {
        var contact = OgcApiContact.fromIndexMap(contactInfo);
        collectionInfo.getContacts().add(contact);
      }
    }
  }

  //override description (abstract) from attached ServiceRecord
  private void handleDescription(CollectionInfo collectionInfo,
      IndexRecord indexRecord) {
    var desc = getLangString(indexRecord.getResourceAbstract());
    if (desc != null) {
      collectionInfo.setDescription(desc);
    }
  }


  //override title from attached ServiceRecord
  private void handleTitle(CollectionInfo collectionInfo, IndexRecord indexRecord) {
    var title = getLangString(indexRecord.getResourceTitle());
    if (title != null) {
      collectionInfo.setTitle(title);
    }
  }
}
