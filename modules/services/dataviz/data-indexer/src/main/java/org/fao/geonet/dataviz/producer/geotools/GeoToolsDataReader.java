/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.producer.geotools;

import java.net.URI;
import java.util.List;
import lombok.NonNull;
import org.fao.geonet.dataviz.model.GeodataRecord;
import org.fao.geonet.dataviz.model.DataQuery;
import org.fao.geonet.dataviz.producer.DatasetReader;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

public class GeoToolsDataReader implements DatasetReader {

  private @Autowired List<GeoToolsFormat> supportedFormats;

  @Override
  public boolean canHandle(URI datasetUri) {
    return supportedFormats.stream().anyMatch(f -> f.canHandle(datasetUri));
  }

  @Override
  public Flux<GeodataRecord> read(@NonNull DataQuery query) {
    final URI datasetUri = query.getUri();
    final GeoToolsFormat formatReader = supportedFormats.stream()
        .filter(f -> f.canHandle(datasetUri)).findFirst()
        .orElseThrow(IllegalArgumentException::new);

    return formatReader.read(query);
  }

}
