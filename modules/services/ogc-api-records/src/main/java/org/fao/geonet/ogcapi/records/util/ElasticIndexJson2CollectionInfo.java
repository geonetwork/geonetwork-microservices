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
   * @param collectionInfo      collection metadata we've gathered so far (usually not much)
   * @param linkedServiceRecord Parsed JSON map of the linked Service record (GN's DB "source"
   *                            "serviceRecord")
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

    set(getTitle(collectionInfo, indexRecord), collectionInfo, "title");
    set(getDescription(collectionInfo, indexRecord), collectionInfo, "description");

    set(getContacts(collectionInfo, indexRecord), collectionInfo, "contacts");

    OgcApiSpatialExtent spatialExtent = getSpatialExtent(indexRecord);
    OgcApiTemporalExtent temporalExtent = getTemporalExtent(indexRecord);
    OgcApiExtent extent = new OgcApiExtent(spatialExtent, temporalExtent);
    if (spatialExtent != null || temporalExtent != null) {
      set(extent,collectionInfo, "extent");
    }

    set(getCrs(collectionInfo, indexRecord), collectionInfo, "crs");
    set(getCreateDate(collectionInfo, indexRecord), collectionInfo, "created");
    set(getChangeDate(collectionInfo, indexRecord), collectionInfo, "updated");
    set(getTags(collectionInfo, indexRecord), collectionInfo, "keywords");

    set(getLanguage(collectionInfo, indexRecord), collectionInfo, "language");
    set(getOtherLangs(collectionInfo, indexRecord), collectionInfo, "languages");

    set(getThemes(collectionInfo, indexRecord), collectionInfo, "themes");

    set(getLicenses(collectionInfo, indexRecord), collectionInfo, "license");
    set(getRights(collectionInfo, indexRecord), collectionInfo, "rights");
  }


  /**
   * use reflection to set a value. We do this, so we can handle all sorts of different object (i.e.
   * geojson) and parts of the various ogcapi specifications (not just record).
   *
   * @param val          object value
   * @param mainObject   object to set the property on
   * @param propertyName name of the property
   */
  public void set(Object val, Object mainObject, String propertyName) {
    if (val == null || mainObject == null || !StringUtils.hasText(propertyName)) {
      return; //nothing to do
    }
    //see if there is a field of that name (should probably cache)
    try {
      var field = mainObject.getClass().getDeclaredField(propertyName);
      field.setAccessible(true);
      field.set(mainObject, val);
    } catch (Exception e) {
      return;
    }

  }



  //its a bit unclear what to do here - there's a big difference between MD record,
  // Elastic Index JSON versus what's expected in the ogcapi license field.  We do the simple
  // action
  private String getRights(CollectionInfo collectionInfo,
      IndexRecord indexRecord) {
    var myLiceseList = indexRecord.getMdLegalConstraintsUseLimitationObject();
    if (myLiceseList != null && !myLiceseList.isEmpty()) {
      if (myLiceseList.size() > 1) {
        var license2 = getLangString(myLiceseList.get(1));
        return license2;
      }
    }
    return null;
  }


  //its a bit unclear what to do here - there's a big difference between MD record,
  // Elastic Index JSON versus what's expected in the ogcapi license field.  We do the simple
  // action
  private String getLicenses(CollectionInfo collectionInfo,
      IndexRecord indexRecord) {
    var myLiceseList = indexRecord.getMdLegalConstraintsUseLimitationObject();
    if (myLiceseList != null && !myLiceseList.isEmpty()) {
      if (myLiceseList.size() > 0) {
        var license = getLangString(myLiceseList.get(0));
        return license;
      }

    }
    return null;
  }

  //process allKeywords to get themes
  private List<OgcApiTheme> getThemes(CollectionInfo collectionInfo,
      IndexRecord indexRecord) {
    var myThemeKeywords = indexRecord.getAllKeywords();
    return OgcApiTheme.parseElasticIndex(myThemeKeywords);
  }

  //process otherLanguage to get the other languages
  private ArrayList<OgcApiLanguage> getOtherLangs(CollectionInfo collectionInfo,
      IndexRecord indexRecord) {
    var langs = indexRecord.getOtherLanguage();
    if (langs != null) {
      var result = new ArrayList<OgcApiLanguage>();
      for (var lang : langs) {
        result.add(new OgcApiLanguage(lang));
      }
      return result;
    }
    return null;
  }

  //process main language
  private OgcApiLanguage getLanguage(CollectionInfo collectionInfo,
      IndexRecord indexRecord) {
    var myMainLang = indexRecord.getMainLanguage();
    if (myMainLang != null && StringUtils.hasText(myMainLang)) {
      return new OgcApiLanguage(myMainLang);
    }
    return null;
  }

  //handle the "tags" to create keywords
  private ArrayList<String> getTags(CollectionInfo collectionInfo, IndexRecord indexRecord) {
    var myTags = indexRecord.getTag();
    if (myTags != null) {
      var result = new ArrayList<String>();
      var tags = myTags;
      collectionInfo.setKeywords(new ArrayList<>());
      for (var tag : tags) {
        result.add(getLangString(tag));
      }
      return result;
    }
    return null;
  }

  //process the change date
  private String getChangeDate(CollectionInfo collectionInfo,
      IndexRecord indexRecord) {
    var updateDate = indexRecord.getChangeDate();
    if (updateDate != null && StringUtils.hasText(updateDate)) {
      return updateDate;
    }
    return null;
  }

  //process the creation date
  private String getCreateDate(CollectionInfo collectionInfo,
      IndexRecord indexRecord) {
    var createDate = indexRecord.getCreateDate();
    if (createDate != null && StringUtils.hasText(createDate)) {
      return createDate;
    }
    return null;
  }

  //process CRS
  private ArrayList<String> getCrs(CollectionInfo collectionInfo, IndexRecord indexRecord) {
    var crss = indexRecord.getCoordinateSystem();
    var result = new ArrayList<String>();
    if (crss != null) {
      for (var crs : crss) {
        result.add(crs);
      }
      return result;
    }
    return null;
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
  private OgcApiTemporalExtent getTemporalExtent(IndexRecord indexRecord) {
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
  private OgcApiSpatialExtent getSpatialExtent(IndexRecord indexRecord) {
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
  private List<OgcApiContact> getContacts(CollectionInfo collectionInfo,
      IndexRecord indexRecord) {
    var contacts = indexRecord.getContact();
    if (contacts != null && !contacts.isEmpty()) {
      var result = new ArrayList<OgcApiContact>();

      for (var contactInfo : contacts) {
        var contact = OgcApiContact.fromIndexMap(contactInfo);
        result.add(contact);
      }
      return result;
    }
    return null;
  }

  //override description (abstract) from attached ServiceRecord
  private String getDescription(CollectionInfo collectionInfo,
      IndexRecord indexRecord) {
    var desc = getLangString(indexRecord.getResourceAbstract());
    return desc;
  }


  //override title from attached ServiceRecord
  private String getTitle(CollectionInfo collectionInfo, IndexRecord indexRecord) {
    var title = getLangString(indexRecord.getResourceTitle());
    return title;
  }
}
