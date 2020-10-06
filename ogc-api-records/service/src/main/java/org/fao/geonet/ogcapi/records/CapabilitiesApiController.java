/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.ogcapi.records;

import io.swagger.annotations.ApiParam;
import org.fao.geonet.ogcapi.records.rest.ogc.model.CollectionInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CapabilitiesApiController implements CapabilitiesApi {

  /**
   * Describe a collection.
   */
  public ResponseEntity<CollectionInfo> describeCollection(
      @ApiParam(value = "Identifier (name) of a specific collection", required = true)
      @PathVariable("collectionId")
      String collectionId) {
    CollectionInfo collectionInfo = new CollectionInfo();
    collectionInfo.name("My GeoNetwork catalog");
    collectionInfo.description("Search for datasets, services and maps.");
    return ResponseEntity.ok(collectionInfo);
  }
}
