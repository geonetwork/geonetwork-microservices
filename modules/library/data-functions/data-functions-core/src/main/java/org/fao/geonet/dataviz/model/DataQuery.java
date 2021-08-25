package org.fao.geonet.dataviz.model;

import java.net.URI;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DataQuery {

  private DataSource source;
  private String layerName;

  public static DataQuery fromUri(URI dataUri) {
    return new DataQuery().setSource(DataSource.fromUri(dataUri));
  }
}
