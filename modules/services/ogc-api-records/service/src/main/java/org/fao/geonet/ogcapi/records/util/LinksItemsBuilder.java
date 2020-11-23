package org.fao.geonet.ogcapi.records.util;

import org.fao.geonet.ogcapi.records.rest.ogc.model.Link;
import org.springframework.http.MediaType;
import java.util.ArrayList;
import java.util.List;

public class LinksItemsBuilder {
  public static List<Link> build(MediaType mediaType, String url, String language) {
    List<Link> links = new ArrayList<>();

    Link currentDoc = new Link();
    currentDoc.setRel("self");
    currentDoc.setHref(url);
    currentDoc.setType(mediaType.toString());
    currentDoc.setHreflang(language);
    links.add(currentDoc);

    for(MediaType supportedMediaType : MediaTypeUtil.supportedMediaTypes) {
      if (!supportedMediaType.equals(mediaType)) {
        Link alternateDoc = new Link();
        alternateDoc.setRel("alternate");
        alternateDoc.setHref(url);
        alternateDoc.setType(supportedMediaType.toString());
        alternateDoc.setHreflang(language);

        links.add(alternateDoc);
      }
    }

    return links;
  }


}
