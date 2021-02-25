package org.fao.geonet.ogcapi.records.util;


import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import org.fao.geonet.domain.Source;
import org.fao.geonet.domain.SourceType;
import org.fao.geonet.ogcapi.records.controller.model.CollectionInfo;
import org.fao.geonet.ogcapi.records.controller.model.Extent;
import org.fao.geonet.ogcapi.records.controller.model.Extent.CrsEnum;
import org.fao.geonet.ogcapi.records.controller.model.Extent.TrsEnum;
import org.fao.geonet.ogcapi.records.controller.model.Link;
import org.springframework.http.MediaType;

public class CollectionInfoBuilder {

  /**
   * Build Collection info from source table.
   */
  public static CollectionInfo buildFromSource(Source source, String language,
      String baseUrl, MediaType mediaType) {
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
    currentDoc.setHref(collectionUri.toString());
    currentDoc.setType(mediaType.toString());
    currentDoc.setHreflang(language);

    List<Link> linkList = LinksItemsBuilder.build(mediaType, collectionUri.toString(), language);
    linkList.forEach(l -> collectionInfo.addLinksItem(l));

    return collectionInfo;
  }
}
