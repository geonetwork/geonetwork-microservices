package org.fao.geonet.dataviz.producer.geotools;

import java.io.File;
import java.net.URI;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.NonNull;
import org.apache.commons.io.FilenameUtils;
import org.fao.geonet.dataviz.model.DataQuery;
import org.geotools.geopkg.GeoPkgDataStoreFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class GeoPackageFormat extends GeoToolsFormat {

  private @Autowired GeoPackageFormatProperties defaults;

  @Override
  public boolean canHandle(@NonNull URI datasetUri) {
    URI uri = URI.create(toURL(datasetUri).toExternalForm());
    if (!"file".equals(uri.getScheme())) {
      return false;
    }
    String fileName = Paths.get(uri).getFileName().toString();
    List<String> gpkgExtensions = defaults.getFileExtensions();
    String extension = FilenameUtils.getExtension(fileName);
    return gpkgExtensions.stream().anyMatch(ext -> ext.equalsIgnoreCase(extension));
  }

  @Override
  protected Map<String, ?> toConnectionParams(@NonNull DataQuery query) {
    final URI datasetUri = query.getUri();
    Objects.requireNonNull(datasetUri, "DataQuery.uri is required");
    URI absoluteUri = URI.create(toURL(datasetUri).toExternalForm());
    File file = new File(absoluteUri);

    Map<String, Object> params = new HashMap<>();
    params.put(GeoPkgDataStoreFactory.DBTYPE.key, "geopkg");
    params.put(GeoPkgDataStoreFactory.DATABASE.key, file);
    params.put(GeoPkgDataStoreFactory.READ_ONLY.key, Boolean.TRUE);
    return params;
  }
}
