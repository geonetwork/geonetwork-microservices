package org.fao.geonet.ogcapi.records.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.fao.geonet.common.search.GnMediaType;
import org.fao.geonet.common.search.SearchConfiguration;
import org.fao.geonet.common.search.SearchConfiguration.Format;
import org.fao.geonet.index.converter.RssConverter;
import org.fao.geonet.ogcapi.records.MvcConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
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

  public static final List<MediaType> ldSupportedMediaTypes =
      Arrays.asList(
          GnMediaType.APPLICATION_JSON_LD,
          GnMediaType.TEXT_TURTLE,
          GnMediaType.APPLICATION_RDF_XML,
          GnMediaType.APPLICATION_DCAT2_XML);


  public static final List<MediaType> xmlMediaTypes =
      Arrays.asList(MediaType.APPLICATION_XML);


  public static MediaType calculatePriorityMediaTypeFromRequest(HttpServletRequest request) {
    return calculatePriorityMediaTypeFromRequest(request, MediaTypeUtil.defaultSupportedMediaTypes);
  }

  @Autowired
  SearchConfiguration searchConfiguration;

  static SearchConfiguration SEARCH_CONFIGURATION;

  @PostConstruct
  void init(){
    SEARCH_CONFIGURATION = searchConfiguration;
  }

  /**
   * From web request, return the supported media type or JSON.
   */
  public static MediaType calculatePriorityMediaTypeFromRequest(HttpServletRequest request,
      List<MediaType> allowedMediaTypes) {

    String requestMediaType = request.getHeader(HttpHeaders.ACCEPT);
    String format = request.getParameter("f");
    if (format != null) {
      Optional<Format> formatConfig = SEARCH_CONFIGURATION.getFormats().stream()
          .filter(f -> f.getName().equals(format)).findFirst();
      if (formatConfig.isPresent()) {
        requestMediaType = formatConfig.get().getMimeType();
      }
    }

    List<MediaType> mediaTypesInRequest = MediaType
        .parseMediaTypes(requestMediaType);

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
