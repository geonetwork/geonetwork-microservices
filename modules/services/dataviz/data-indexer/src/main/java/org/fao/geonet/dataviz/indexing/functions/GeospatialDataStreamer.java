/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.indexing.functions;

import java.net.URI;
import java.util.function.Function;
import lombok.NonNull;
import reactor.core.publisher.Flux;

public class GeospatialDataStreamer implements Function<URI, Flux<GeodataRecord>> {

  @Override
  public Flux<GeodataRecord> apply(@NonNull URI datasetUri) {
    // TODO Auto-generated method stub
    return null;
  }

}
