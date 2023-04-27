package org.fao.geonet.ogcapi.records.util;


import static org.fao.geonet.ogcapi.records.util.LinksItemsBuilder.getHref;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.fao.geonet.common.search.SearchConfiguration;
import org.fao.geonet.common.search.SearchConfiguration.Format;
import org.fao.geonet.domain.Source;
import org.fao.geonet.domain.SourceType;
import org.fao.geonet.ogcapi.records.controller.model.CollectionInfo;
import org.fao.geonet.ogcapi.records.controller.model.Extent;
import org.fao.geonet.ogcapi.records.controller.model.Extent.CrsEnum;
import org.fao.geonet.ogcapi.records.controller.model.Extent.TrsEnum;
import org.fao.geonet.ogcapi.records.controller.model.Link;

public class CollectionInfoBuilder {

  private CollectionInfoBuilder() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Build Collection info from source table.
   */
  public static CollectionInfo buildFromSource(Source source, String language,
      String baseUrl, Optional<Format> format, SearchConfiguration configuration) {
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
        .setCrs(Arrays.asList(CrsEnum.HTTP_WWW_OPENGIS_NET_DEF_CRS_OGC_1_3_CRS84.getValue()));

    // TODO: Review values
    Extent extent = new Extent();
    extent.crs(CrsEnum.HTTP_WWW_OPENGIS_NET_DEF_CRS_OGC_1_3_CRS84);
    extent.setSpatial(Arrays.asList(new BigDecimal(-180), new BigDecimal(-90),
        new BigDecimal(180), new BigDecimal(90)));
    collectionInfo.setExtent(extent);

    extent.setTrs(TrsEnum.HTTP_WWW_OPENGIS_NET_DEF_UOM_ISO_8601_0_GREGORIAN);

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

    return collectionInfo;
  }
}
