package org.fao.geonet.dataviz.consumer.geotools;

import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.NonNull;
import org.fao.geonet.dataviz.model.DataSource;
import org.fao.geonet.dataviz.model.GeodataRecord;

public class GeoToolsConsumers {

  public Consumer<Stream<GeodataRecord>> importTo(DataSource target) {
    throw new UnsupportedOperationException();
  }

  public void index(@NonNull Stream<GeodataRecord> records) {
    throw new UnsupportedOperationException();
  }
}
