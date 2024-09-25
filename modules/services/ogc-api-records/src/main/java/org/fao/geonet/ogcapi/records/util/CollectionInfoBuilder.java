/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.util;


import static java.util.Arrays.asList;
import static org.fao.geonet.ogcapi.records.util.LinksItemsBuilder.getHref;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.common.search.SearchConfiguration;
import org.fao.geonet.common.search.SearchConfiguration.Format;
import org.fao.geonet.domain.Source;
import org.fao.geonet.domain.SourceType;
import org.fao.geonet.ogcapi.records.controller.model.CollectionInfo;
import org.fao.geonet.ogcapi.records.controller.model.CrsEnum;
import org.fao.geonet.ogcapi.records.model.OgcApiExtent;
import org.fao.geonet.ogcapi.records.model.OgcApiLink;
import org.fao.geonet.ogcapi.records.model.OgcApiSpatialExtent;
import org.fao.geonet.ogcapi.records.service.RecordService;
import org.fao.geonet.repository.SettingRepository;
import org.fao.geonet.repository.SourceRepository;
import org.fao.geonet.view.ViewUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j(topic = "org.fao.geonet.ogcapi.records")
public class CollectionInfoBuilder {

  @Value("${gn.legacy.url}")
  String geonetworkUrl;

  @Autowired
  RecordService recordService;
  @Autowired
  ElasticIndexJson2CollectionInfo elasticIndexJson2CollectionInfo;


  public CollectionInfoBuilder() {

  }


  /**
   * Build Collection info from source table.
   *
   * @param source        from the GN DB Table "sources"
   * @param language      which language is the request wanting
   * @param baseUrl       base url for the ogcapi
   * @param format        what format are the results requested in
   * @param configuration config for searching
   * @param request       user request (for security)
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
    OgcApiLink currentDoc = new OgcApiLink();
    currentDoc.setRel("self");
    currentDoc.setHref(getHref(collectionUri.toString(), format));
    currentDoc.setType(format.get().getMimeType());
    currentDoc.setHreflang(language);

    List<OgcApiLink> linkList = LinksItemsBuilder.build(
        format, collectionUri.toString(), language, configuration);
    linkList.forEach(collectionInfo::addLinksItem);


    var gnbase = geonetworkUrl;
    if (!gnbase.endsWith("/")) {
      gnbase += "/";
    }
    //assume its a png
    var url = URI.create(gnbase).resolve("images/logos/" + source.getUuid() + ".png");
    var link = new OgcApiLink();
    link.setHref(url.toString());
    link.setRel("icon");
    link.setType("image/png");
    collectionInfo.addLinksItem(link);


    var linkedServiceRecord =
        recordService.getLinkedServiceRecord(request, source);
    elasticIndexJson2CollectionInfo.injectLinkedServiceRecordInfo(collectionInfo,
        linkedServiceRecord);

    return collectionInfo;
  }


}
