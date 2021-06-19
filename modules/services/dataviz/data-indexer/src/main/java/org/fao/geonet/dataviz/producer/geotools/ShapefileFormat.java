package org.fao.geonet.dataviz.producer.geotools;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.geotools.data.shapefile.ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX;
import static org.geotools.data.shapefile.ShapefileDataStoreFactory.DBFCHARSET;
import static org.geotools.data.shapefile.ShapefileDataStoreFactory.ENABLE_SPATIAL_INDEX;
import static org.geotools.data.shapefile.ShapefileDataStoreFactory.FILE_TYPE;
import static org.geotools.data.shapefile.ShapefileDataStoreFactory.URLP;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.NonNull;
import org.fao.geonet.dataviz.model.DataQuery;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ShapefileFormat extends GeoToolsFormat {

  private @Autowired ShapefileFormatProperties defaults;

  private static final ShapefileDataStoreFactory FACTORY = new ShapefileDataStoreFactory();

  @Override
  public boolean canHandle(@NonNull URI datasetUri) {
    return FACTORY.canProcess(toURL(datasetUri));
  }

  @Override
  protected Map<String, ?> toConnectionParams(@NonNull DataQuery query) {
    final URI datasetUri = query.getUri();
    Objects.requireNonNull(datasetUri, "DataQuery.uri is required");
    URL url = toURL(datasetUri);

    Map<String, Object> params = new HashMap<>();
    params.put(FILE_TYPE.key, "shapefile");
    params.put(URLP.key, url);
    Charset charset = query.getEncoding() == defaults.getDefaultCharset() ? UTF_8
        : query.getEncoding();
    if (charset != null) {
      params.put(DBFCHARSET.key, charset);
    }
    // this is a full-table-scan read, don't make the store create a spatial index
    params.put(ENABLE_SPATIAL_INDEX.key, Boolean.FALSE);
    params.put(CREATE_SPATIAL_INDEX.key, Boolean.FALSE);
    return params;
  }
}
