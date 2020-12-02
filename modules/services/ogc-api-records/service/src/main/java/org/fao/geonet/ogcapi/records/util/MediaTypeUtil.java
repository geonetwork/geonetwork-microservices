package org.fao.geonet.ogcapi.records.util;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.WebRequest;

public class MediaTypeUtil {

  public static final List<MediaType> supportedMediaTypes =
      Arrays.asList(
          MediaType.APPLICATION_JSON,
          MediaType.APPLICATION_XML,
          MediaType.TEXT_HTML);

  public static boolean isSupportedMediaType(MediaType mediaType) {
    return supportedMediaTypes.contains(mediaType);
  }

  /**
   * From web request, return the supported media type or JSON.
   */
  public static MediaType calculatePriorityMediaTypeFromRequest(WebRequest request) {
    List<MediaType> mediaTypesInRequest = MediaType
        .parseMediaTypes(request.getHeader(HttpHeaders.ACCEPT));

    MediaType priorityMediatype = null;
    for (MediaType mediaType : mediaTypesInRequest) {
      if (isSupportedMediaType(mediaType)) {
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
