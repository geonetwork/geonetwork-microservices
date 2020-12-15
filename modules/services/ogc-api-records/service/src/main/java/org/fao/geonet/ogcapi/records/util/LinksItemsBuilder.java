package org.fao.geonet.ogcapi.records.util;

import java.util.ArrayList;
import java.util.List;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;

public class LinksItemsBuilder {

  /**
   * Build items.
   */
  public static List<Link> build(MediaType mediaType, String url, String language) {
    List<Link> links = new ArrayList<>();

    Link currentDoc = Link.of(url).withSelfRel()
        .withMedia(mediaType.toString()).withHreflang(language)
        .withTitle("This document as " +  mediaType.toString());
    links.add(currentDoc);

    for (MediaType supportedMediaType : MediaTypeUtil.supportedMediaTypes) {
      if (!supportedMediaType.equals(mediaType)) {
        Link alternateDoc = Link.of(url).withRel("alternate")
            .withMedia(supportedMediaType.toString()).withHreflang(language)
            .withTitle("This document as " + supportedMediaType.toString());
        links.add(alternateDoc);
      }
    }

    return links;
  }


}
