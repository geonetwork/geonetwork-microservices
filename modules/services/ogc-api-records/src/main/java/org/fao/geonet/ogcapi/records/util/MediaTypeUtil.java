package org.fao.geonet.ogcapi.records.util;

import java.util.Arrays;
import java.util.List;
import org.fao.geonet.common.search.GnMediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.WebRequest;
import javax.print.attribute.standard.Media;
import javax.servlet.http.HttpServletRequest;

public class MediaTypeUtil {

  public static final List<MediaType> defaultSupportedMediaTypes =
      Arrays.asList(
          MediaType.APPLICATION_JSON,
          MediaType.APPLICATION_XML,
          MediaType.TEXT_HTML);

  public static final List<MediaType> openSearchSupportedMediaTypes =
      Arrays.asList(
          MediaType.APPLICATION_RSS_XML,
          MediaType.APPLICATION_ATOM_XML,
          GnMediaType.APPLICATION_OPENSEARCH_XML);

  public static final List<MediaType> jsonLdSupportedMediaTypes =
      Arrays.asList(
          GnMediaType.APPLICATION_JSON_LD,
          GnMediaType.TEXT_TURTLE,
          GnMediaType.APPLICATION_RDF_XML);


  public static final List<MediaType> xmlDcatMediaTypes =
      Arrays.asList(
          MediaType.APPLICATION_XML,
          GnMediaType.APPLICATION_GN_XML,
          GnMediaType.APPLICATION_DCAT2_XML);


  public static MediaType calculatePriorityMediaTypeFromRequest(HttpServletRequest request) {
    return calculatePriorityMediaTypeFromRequest(request, MediaTypeUtil.defaultSupportedMediaTypes);
  }
  /**
   * From web request, return the supported media type or JSON.
   */
  public static MediaType calculatePriorityMediaTypeFromRequest(HttpServletRequest request, List<MediaType> allowedMediaTypes) {
    List<MediaType> mediaTypesInRequest = MediaType
        .parseMediaTypes(request.getHeader(HttpHeaders.ACCEPT));

    MediaType priorityMediatype = null;
    for (MediaType mediaType : mediaTypesInRequest) {
      if (allowedMediaTypes.contains(mediaType)) {
        priorityMediatype = mediaType;
        break;
      }
    }

    if (priorityMediatype == null) {
      priorityMediatype = MediaType.APPLICATION_JSON;
    }

    return priorityMediatype;
  }


}
