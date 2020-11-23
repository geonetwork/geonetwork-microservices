package org.fao.geonet.ogcapi.records;

import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.fao.geonet.domain.Source;
import org.fao.geonet.domain.SourceType;
import org.fao.geonet.ogcapi.records.rest.ogc.model.CollectionInfo;
import org.fao.geonet.ogcapi.records.util.CollectionInfoBuilder;
import org.fao.geonet.ogcapi.records.util.MediaTypeUtil;
import org.fao.geonet.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;


@Controller
public class CollectionApiController implements CollectionApi {

  @Autowired
  private NativeWebRequest nativeWebRequest;

  @Autowired
  private SourceRepository sourceRepository;

  /**
   * Describe a collection.
   */
  @Override
  public ResponseEntity<CollectionInfo> describeCollection(
      @ApiParam(value = "Identifier (name) of a specific collection", required = true)
      @PathVariable("collectionId") String collectionId) {

    Locale locale = LocaleContextHolder.getLocale();
    String language = locale.getISO3Language();

    MediaType mediaType = MediaTypeUtil.calculatePriorityMediaTypeFromRequest(nativeWebRequest);

    Source source = null;

    if (collectionId.equals("main")) {
      List<Source> sources = sourceRepository.findByType(SourceType.portal, null);
      if (!sources.isEmpty()) {
        source = sources.get(0);
      }
    } else {
      source = sourceRepository.findOneByUuid(collectionId);
    }

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find collection");
    }

    String baseUrl = ((HttpServletRequest) nativeWebRequest.getNativeRequest()).getRequestURL()
        .toString();

    CollectionInfo collectionInfo = CollectionInfoBuilder
        .buildFromSource(source, language, baseUrl, mediaType);

    return ResponseEntity.ok(collectionInfo);
  }
}
