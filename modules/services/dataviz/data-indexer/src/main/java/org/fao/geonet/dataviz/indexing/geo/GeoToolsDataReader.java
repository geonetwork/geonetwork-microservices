/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.indexing.geo;

import java.net.URI;
import lombok.NonNull;
import org.fao.geonet.dataviz.indexing.functions.GeodataRecord;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class GeoToolsDataReader implements DatasetReader {

  @Override
  public boolean canHandle(URI datasetUri) {
    return false;
  }

  @Override
  public Flux<GeodataRecord> read(@NonNull URI datasetUri) {
    throw new UnsupportedOperationException();
  }

}
