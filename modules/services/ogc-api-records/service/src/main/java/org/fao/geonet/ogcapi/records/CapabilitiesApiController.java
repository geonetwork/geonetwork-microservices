/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.ogcapi.records;

import io.swagger.annotations.ApiParam;
import java.util.Optional;
import org.fao.geonet.ogcapi.records.rest.ogc.model.CollectionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.NativeWebRequest;

@Controller
public class CapabilitiesApiController implements CapabilitiesApi {

  /**
   * Only to support sample responses from {@link CapabilitiesApi}, remove once
   * all its methods are implemented.
   */
  private @Autowired NativeWebRequest nativeWebRequest;

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.of(nativeWebRequest);
  }

  /**
   * Describe a collection.
   */
  public ResponseEntity<CollectionInfo> describeCollection(
      @ApiParam(value = "Identifier (name) of a specific collection", required = true) 
      @PathVariable("collectionId") String collectionId) {
    CollectionInfo collectionInfo = new CollectionInfo();
    collectionInfo.name("My GeoNetwork catalog");
    collectionInfo.description("Search for datasets, services and maps.");
    return ResponseEntity.ok(collectionInfo);
  }
}
