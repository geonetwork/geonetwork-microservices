package org.fao.geonet.ogcapi.records.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.fao.geonet.common.search.SearchConfiguration;
import org.fao.geonet.common.search.SearchConfiguration.Format;
import org.fao.geonet.ogcapi.records.model.OgcApiLink;
import org.springframework.http.MediaType;

public class LinksItemsBuilder {

  private LinksItemsBuilder() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Build items.
   */
  public static List<OgcApiLink> build(
      Optional<Format> format, String url, String language,
      SearchConfiguration configuration) {
    OgcApiLink currentDoc = new OgcApiLink();
    Format linkFormat = format.get();
    currentDoc.setRel("self");
    currentDoc.setHref(getHref(url, format));
    currentDoc.setType(format.get().getMimeType());
    currentDoc.setHreflang(language);

    List<OgcApiLink> links = new ArrayList<>();
    links.add(currentDoc);

    for (MediaType supportedMediaType : MediaTypeUtil.defaultSupportedMediaTypes) {
      if (!supportedMediaType.toString().equals(linkFormat.getMimeType())) {
        Optional<Format> f = configuration.getFormat(supportedMediaType);
        OgcApiLink alternateDoc = new OgcApiLink();
        alternateDoc.setRel("alternate");
        alternateDoc.setHref(getHref(url, f));
        alternateDoc.setType(supportedMediaType.toString());
        alternateDoc.setHreflang(language);

        links.add(alternateDoc);
      }
    }
    return links;
  }

  public static String getHref(String url, Optional<Format> format) {
    return url
        + (format.isPresent() ? "?f=" + format.get().getName() : "");
  }
}