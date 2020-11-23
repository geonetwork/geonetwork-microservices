package org.fao.geonet.ogcapi.records.util;


import org.fao.geonet.domain.Source;
import org.fao.geonet.domain.SourceType;
import org.fao.geonet.ogcapi.records.rest.ogc.model.CollectionInfo;
import org.fao.geonet.ogcapi.records.rest.ogc.model.Extent;
import org.fao.geonet.ogcapi.records.rest.ogc.model.Extent.CrsEnum;
import org.fao.geonet.ogcapi.records.rest.ogc.model.Extent.TrsEnum;
import org.fao.geonet.ogcapi.records.rest.ogc.model.Link;
import org.springframework.http.MediaType;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class CollectionInfoBuilder {

  public static CollectionInfo buildFromSource(Source source, String language,
      String baseUrl, MediaType mediaType) {
    String name;

    if (source.getType() == SourceType.portal) {
      name = "main";
    } else {
      name = source.getUuid();
    }

    URI collectionUri = URI.create(baseUrl).resolve(name);

    CollectionInfo collectionInfo = new CollectionInfo();
    collectionInfo.setName(name);
    collectionInfo.setDescription(source.getLabel(language));
    collectionInfo.setTitle(source.getLabel(language));
    collectionInfo.setCrs(Arrays.asList(CrsEnum.HTTP_WWW_OPENGIS_NET_DEF_CRS_OGC_1_3_CRS84.getValue()));

    // TODO: Review values
    Extent extent = new Extent();
    extent.crs(CrsEnum.HTTP_WWW_OPENGIS_NET_DEF_CRS_OGC_1_3_CRS84);
    extent.setSpatial(Arrays.asList(new BigDecimal(-180), new BigDecimal(-90),
        new BigDecimal(180), new BigDecimal(90)));
    collectionInfo.setExtent(extent);

    extent.setTrs(TrsEnum.HTTP_WWW_OPENGIS_NET_DEF_UOM_ISO_8601_0_GREGORIAN);

    // TODO: Accept format parameter.
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
