package org.fao.geonet.ogcapi.records.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.fao.geonet.domain.Source;
import org.fao.geonet.domain.SourceType;
import org.fao.geonet.index.JsonUtils;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.ogcapi.records.controller.model.CollectionInfo;
import org.fao.geonet.ogcapi.records.controller.model.CollectionInfoExtended;
import org.fao.geonet.ogcapi.records.controller.model.Extent;
import org.fao.geonet.ogcapi.records.controller.model.Extent.CrsEnum;
import org.fao.geonet.ogcapi.records.controller.model.Extent.TrsEnum;
import org.fao.geonet.ogcapi.records.controller.model.Link;
import org.fao.geonet.ogcapi.records.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class CollectionInfoBuilder {

  @Autowired
  CollectionService collectionService;

  /**
   * Build Collection info lists from sources table.
   */
  public List<CollectionInfo> buildFromSources(HttpServletRequest request,
      List<Source> sources, String language, String baseUrl, MediaType mediaType) {

    List<CollectionInfo> collectionInfoList = new ArrayList<>();

    sources.forEach(s -> {
      IndexRecord serviceRecord = collectionService
          .retrieveServiceMetadataForCollection(request, s);

      collectionInfoList.add(
          buildFromSource(s, serviceRecord, language, baseUrl, mediaType));
    });

    return collectionInfoList;
  }

  /**
   * Build Collection info extended list from source table.
   */
  public List<CollectionInfoExtended> buildExtendedFromSources(HttpServletRequest request,
      List<Source> sources, String language, String baseUrl, MediaType mediaType) {

    List<CollectionInfoExtended> collectionInfoList = new ArrayList<>();

    sources.forEach(s -> {
      IndexRecord serviceRecord = collectionService
          .retrieveServiceMetadataForCollection(request, s);

      collectionInfoList.add(
          buildExtendedFromSource(s, serviceRecord, language, baseUrl, mediaType));
    });

    return collectionInfoList;
  }

  /**
   * Build Collection info from source table.
   */
  public CollectionInfo buildFromSource(Source source, IndexRecord serviceRecord, String language,
      String baseUrl, MediaType mediaType) {
    String name;

    CollectionInfo collectionInfo;

    if (serviceRecord != null) {
      collectionInfo = buildFromServiceRecordInfo(serviceRecord, language);
    } else {
      collectionInfo = buildFromSourceInfo(source, language);
    }

    if (source.getType() == SourceType.portal) {
      name = "main";
    } else {
      name = source.getUuid();
    }
    collectionInfo.setId(name);

    // TODO: Accept format parameter.
    baseUrl = baseUrl + (!baseUrl.endsWith("/") ? "/" : "");
    URI collectionUri = URI.create(baseUrl).resolve(name);
    Link currentDoc = new Link();
    currentDoc.setRel("self");
    currentDoc.setHref(collectionUri.toString());
    currentDoc.setType(mediaType.toString());
    currentDoc.setHreflang(language);

    List<Link> linkList = LinksItemsBuilder.build(mediaType, collectionUri.toString(), language);
    linkList.forEach(l -> collectionInfo.addLinksItem(l));

    return collectionInfo;
  }

  /**
   * Build Collection info extended from source table.
   */
  public CollectionInfoExtended buildExtendedFromSource(Source source, IndexRecord serviceRecord,
      String language, String baseUrl, MediaType mediaType) {
    
    CollectionInfo collectionInfo =
        buildFromSource(source, serviceRecord, language, baseUrl, mediaType);

    CollectionInfoExtended collectionInfoExtended = CollectionInfoExtended.from(collectionInfo);
    collectionInfoExtended.setSource(source);
    collectionInfoExtended.setRecord(serviceRecord);
    
    return collectionInfoExtended;
  }

  
  private CollectionInfo buildFromSourceInfo(Source source, String language) {
    CollectionInfo collectionInfo = new CollectionInfo();

    String label = source.getLabel(language);
    // The source label may contain a description
    // eg. "INSPIRE|Data sets and services for the environment"
    String[] titleAndDescription = label.split("\\|");
    collectionInfo.setDescription(
        titleAndDescription.length > 1 ? titleAndDescription[1] : "");
    collectionInfo.setTitle(
        titleAndDescription.length > 1 ? titleAndDescription[0] : label);
    collectionInfo
        .setCrs(Arrays.asList(CrsEnum.HTTP_WWW_OPENGIS_NET_DEF_CRS_OGC_1_3_CRS84.getValue()));

    Extent extent = new Extent();
    extent.crs(CrsEnum.HTTP_WWW_OPENGIS_NET_DEF_CRS_OGC_1_3_CRS84);
    extent.setTrs(TrsEnum.HTTP_WWW_OPENGIS_NET_DEF_UOM_ISO_8601_0_GREGORIAN);
    extent.setSpatial(Arrays.asList(new BigDecimal(-180), new BigDecimal(-90),
        new BigDecimal(180), new BigDecimal(90)));

    collectionInfo.setExtent(extent);
    collectionInfo.setCrs(Arrays.asList(extent.getCrs().getValue()));

    return collectionInfo;
  }

  private CollectionInfo buildFromServiceRecordInfo(IndexRecord serviceRecord, String language) {
    CollectionInfo collectionInfo = new CollectionInfo();

    collectionInfo.setTitle(serviceRecord.getResourceTitle().get(language));
    collectionInfo.setDescription(serviceRecord.getResourceAbstract().get(language));

    Extent extent = new Extent();
    extent.crs(CrsEnum.HTTP_WWW_OPENGIS_NET_DEF_CRS_OGC_1_3_CRS84);
    extent.setTrs(TrsEnum.HTTP_WWW_OPENGIS_NET_DEF_UOM_ISO_8601_0_GREGORIAN);

    List<BigDecimal> coordinates = parseRecordCoordinates(serviceRecord);

    if (!coordinates.isEmpty()) {
      extent.setSpatial(coordinates);
    } else {
      extent.setSpatial(Arrays.asList(new BigDecimal(-180), new BigDecimal(-90),
          new BigDecimal(180), new BigDecimal(90)));
    }

    collectionInfo.setCrs(Arrays.asList(extent.getCrs().getValue()));

    collectionInfo.setExtent(extent);

    return collectionInfo;
  }

  private List<BigDecimal> parseRecordCoordinates(IndexRecord record) {
    List<BigDecimal> coordinatesList = new ArrayList<>();

    if (record.getGeometries().size() > 0) {
      String coordinates = record.getGeometries().get(0);

      try {
        ObjectMapper mapper =  JsonUtils.getObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser(coordinates);
        JsonNode coordinatesAsJson = mapper.readTree(parser);

        JsonNode coordinatesNode = coordinatesAsJson.get("coordinates");

        if ((coordinatesNode != null) && (coordinatesNode.size() > 0)) {
          JsonNode coordinatesArrayNode = coordinatesNode.get(0);

          if (coordinatesArrayNode.isArray()) {
            coordinatesList.add(new BigDecimal(coordinatesArrayNode.get(0).get(0).doubleValue()));
            coordinatesList.add(new BigDecimal(coordinatesArrayNode.get(0).get(1).doubleValue()));
            coordinatesList.add(new BigDecimal(coordinatesArrayNode.get(2).get(0).doubleValue()));
            coordinatesList.add(new BigDecimal(coordinatesArrayNode.get(2).get(1).doubleValue()));
          }
        }

      } catch (Exception ex) {
        // TODO: Log exception
      }

    }

    return coordinatesList;
  }
}
