package org.fao.geonet.ogcapi.records.util;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.WebRequest;

public class MediaTypeUtil {

  public static final List<MediaType> supportedMediaTypes =
      Arrays.asList(
          MediaType.APPLICATION_JSON,
          MediaType.APPLICATION_XML,
          MediaType.TEXT_HTML);

  public static final List<String> supportedFormats =
      Arrays.asList("json", "xml", "html");

  public static boolean isSupportedMediaType(MediaType mediaType) {
    return supportedMediaTypes.contains(mediaType);
  }

  public static boolean isSupportedFormat(String format) {
    return supportedFormats.contains(format);
  }

  /**
   * From web request, return the supported media type or JSON.
   */
  public static MediaType calculatePriorityMediaTypeFromRequest(WebRequest request) {
    MediaType priorityMediatype = null;

    List<MediaType> mediaTypesInRequest = MediaType
        .parseMediaTypes(request.getHeader(HttpHeaders.ACCEPT));

    String formatParam = request.getParameter("f");

    if (StringUtils.isNotEmpty(formatParam) && isSupportedFormat(formatParam)) {
      if (formatParam.equals("json")) {
        priorityMediatype = MediaType.APPLICATION_JSON;
      } else  if (formatParam.equals("xml")) {
        priorityMediatype = MediaType.APPLICATION_XML;
      } else {
        priorityMediatype = MediaType.TEXT_HTML;
      }
    }

    if (priorityMediatype == null) {
      for (MediaType mediaType : mediaTypesInRequest) {
        if (isSupportedMediaType(mediaType)) {
          priorityMediatype = mediaType;
          break;
        }
      }

      if (priorityMediatype == null) {
        priorityMediatype = MediaType.APPLICATION_JSON;
      }
    }

    return priorityMediatype;
  }


}
