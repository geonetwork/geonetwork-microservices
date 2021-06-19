package org.fao.geonet.dataviz.producer.geotools;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.dataviz.model.DataQuery;
import org.geotools.data.geojson.GeoJSONDataStoreFactory;

@Slf4j
public class GeoJsonFormat extends GeoToolsFormat {

  private static final GeoJSONDataStoreFactory FACTORY = new GeoJSONDataStoreFactory();

  @Override
  public boolean canHandle(@NonNull URI datasetUri) {
    try {
      return FACTORY.canProcess(toURL(datasetUri));
    } catch (RuntimeException e) {
      log.info("Error getting URL from URI", e);
      return false;
    }
  }

  @Override
  protected Map<String, ?> toConnectionParams(@NonNull DataQuery query) {
    final URI datasetUri = query.getUri();
    Objects.requireNonNull(datasetUri, "DataQuery.uri is required");
    URL url = toURL(datasetUri);
    Map<String, Object> params = new HashMap<>();
    params.put(GeoJSONDataStoreFactory.URL_PARAM.key, url);
    return params;
  }
}
