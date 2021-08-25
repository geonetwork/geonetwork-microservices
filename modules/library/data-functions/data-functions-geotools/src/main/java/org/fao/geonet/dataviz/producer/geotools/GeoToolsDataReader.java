/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.producer.geotools;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.dataviz.model.DataQuery;
import org.fao.geonet.dataviz.model.GeodataRecord;
import org.fao.geonet.dataviz.producer.DatasetReader;

@Slf4j
@ToString
public class GeoToolsDataReader implements DatasetReader {

  private @Getter @Setter List<GeoToolsFormat> supportedFormats = new ArrayList<>();

  @Override
  public boolean canHandle(URI datasetUri) {
    return supportedFormats.stream().anyMatch(f -> f.canHandle(datasetUri));
  }

  @Override
  public Stream<GeodataRecord> read(@NonNull DataQuery query) {
    final URI datasetUri = query.getSource().getUri();
    final GeoToolsFormat formatReader = findReader(datasetUri);
    return formatReader.read(query);
  }

  private GeoToolsFormat findReader(final URI datasetUri) {
    log.debug("Looking up a format reader for {}", datasetUri);
    final GeoToolsFormat formatReader = supportedFormats.stream()
        .filter(f -> f.canHandle(datasetUri)).findFirst()
        .orElseThrow(IllegalArgumentException::new);
    log.debug("Found format reader {} for {}", formatReader.getClass().getSimpleName(), datasetUri);
    return formatReader;
  }

  @Override
  public String getName() {
    return String.format("GeoTools[%s]",
        supportedFormats.stream().map(GeoToolsFormat::getName).collect(Collectors.joining(",")));
  }

}
