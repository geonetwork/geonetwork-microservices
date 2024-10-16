/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.converter;

import java.util.ArrayList;
import org.fao.geonet.domain.Metadata;
import org.fao.geonet.index.converter.IGeoJsonConverter;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.ogcapi.records.controller.model.CollectionInfo;
import org.fao.geonet.ogcapi.records.model.GeoJsonPolygon;
import org.fao.geonet.ogcapi.records.model.OgcApiTime;
import org.fao.geonet.ogcapi.records.util.ElasticIndexJson2CollectionInfo;
import org.fao.geonet.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Converts an elastic JSON Index Record into an OGCAPI compliant GeoJSON record.
 */
@Component
public class OgcApiGeoJsonConverter implements IGeoJsonConverter {

  @Autowired
  ElasticIndexJson2CollectionInfo elasticIndexJson2CollectionInfo;

  @Autowired
  MetadataRepository metadataRepository;

  /**
   * converts an IndexRecord (elastic Index Json Record) to a OgcApiGeoJsonRecord.
   * @param elasticIndexJsonRecord IndexRecord from Elastic
   * @return converted to OgcApiGeoJsonRecord.
   */
  public OgcApiGeoJsonRecord convert(IndexRecord elasticIndexJsonRecord) {
    var result = new OgcApiGeoJsonRecord();

    injectCollectionInfoProperties(result, elasticIndexJsonRecord);

    result.setConformsTo(new ArrayList<>());
    result.getConformsTo()
        .add("http://www.opengis.net/spec/ogcapi-records-1/1.0/req/record-core");

    result.setProperty("gn-elastic-index-record", elasticIndexJsonRecord);

    Metadata metadataRecord = metadataRepository.findOneByUuid(
        elasticIndexJsonRecord.getMetadataIdentifier());

    result.setProperty("gn-record-xml", metadataRecord.getData());
    return result;
  }

  private void injectCollectionInfoProperties(OgcApiGeoJsonRecord result,
      IndexRecord elasticIndexJsonRecord) {
    var collectionInfo = new CollectionInfo();
    elasticIndexJson2CollectionInfo.injectLinkedServiceRecordInfo(collectionInfo,
        elasticIndexJsonRecord);

    result.setId(collectionInfo.getId());
    result.setProperty("title", collectionInfo.getTitle());
    result.setProperty("description", collectionInfo.getTitle());
    result.setProperty("language", collectionInfo.getLanguage());
    result.setProperty("languages", collectionInfo.getLanguages());
    result.setProperty("created", collectionInfo.getCreated());
    result.setProperty("updated", collectionInfo.getUpdated());
    result.setProperty("type", collectionInfo.getType());
    result.setProperty("keywords", collectionInfo.getKeywords());
    result.setProperty("themes", collectionInfo.getThemes());
    result.setProperty("resourceLanguages", collectionInfo.getRecordLanguages());
    result.setProperty("contacts", collectionInfo.getContacts());
    result.setProperty("license", collectionInfo.getLicense());
    result.setProperty("rights", collectionInfo.getRights());

    if (collectionInfo.getExtent() != null
        && collectionInfo.getExtent().getSpatial() != null
        && collectionInfo.getExtent().getSpatial().getBbox() != null
        && !collectionInfo.getExtent().getSpatial().getBbox().isEmpty()) {
      result.setGeometry(
          GeoJsonPolygon.fromBBox(collectionInfo.getExtent().getSpatial().getBbox()));
    }

    if (collectionInfo.getExtent() != null
        && collectionInfo.getExtent().getTemporal() != null
        && collectionInfo.getExtent().getTemporal().getInterval() != null
        && !collectionInfo.getExtent().getTemporal().getInterval().isEmpty()) {
      var time = new OgcApiTime();
      time.setInterval(collectionInfo.getExtent().getTemporal().getInterval());
      result.setTime(time);
    }
  }

}
