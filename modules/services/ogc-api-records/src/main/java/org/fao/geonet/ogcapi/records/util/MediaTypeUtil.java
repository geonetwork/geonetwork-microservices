/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.fao.geonet.common.search.GnMediaType;
import org.fao.geonet.common.search.SearchConfiguration;
import org.fao.geonet.common.search.SearchConfiguration.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class MediaTypeUtil {

  @Autowired
  SearchConfiguration searchConfiguration;

  public static final List<MediaType> defaultSupportedMediaTypes =
      List.of(
          MediaType.APPLICATION_JSON,
          MediaType.APPLICATION_XML,
          MediaType.TEXT_HTML);

  public static final List<MediaType> openSearchSupportedMediaTypes =
      List.of(
          MediaType.APPLICATION_RSS_XML,
          MediaType.APPLICATION_ATOM_XML,
          GnMediaType.APPLICATION_OPENSEARCH_XML);

  public static final List<MediaType> ldSupportedMediaTypes =
      List.of(
          GnMediaType.APPLICATION_JSON_LD,
          GnMediaType.TEXT_TURTLE,
          GnMediaType.APPLICATION_RDF_XML,
          GnMediaType.APPLICATION_DCAT2_XML);


  public static final List<MediaType> xmlMediaTypes =
      List.of(MediaType.APPLICATION_XML);


  public MediaType calculatePriorityMediaTypeFromRequest(HttpServletRequest request) {
    return calculatePriorityMediaTypeFromRequest(request, MediaTypeUtil.defaultSupportedMediaTypes);
  }

  /**
   * From web request, return the supported media type or JSON.
   */
  public MediaType calculatePriorityMediaTypeFromRequest(HttpServletRequest request,
      List<MediaType> allowedMediaTypes) {

    String formatValue = request.getParameter("f");

    List<MediaType> mediaTypesInRequest = new ArrayList<>();

    if (StringUtils.isNotEmpty(formatValue)) {
      Optional<Format> format = searchConfiguration.getFormats()
          .stream().filter(f -> f.getName().equals(formatValue)).findFirst();

      if (format.isPresent()) {
        mediaTypesInRequest =  MediaType
            .parseMediaTypes(format.get().getMimeType());
      }
    }

    if (mediaTypesInRequest.isEmpty()) {
      mediaTypesInRequest = MediaType
          .parseMediaTypes(request.getHeader(HttpHeaders.ACCEPT));
    }

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
