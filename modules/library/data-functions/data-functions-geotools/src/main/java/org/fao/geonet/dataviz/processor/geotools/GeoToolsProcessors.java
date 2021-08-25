package org.fao.geonet.dataviz.processor.geotools;

import java.util.function.Function;
import java.util.stream.Stream;
import lombok.NonNull;
import org.fao.geonet.dataviz.model.GeodataRecord;
import org.locationtech.jts.geom.Geometry;

public class GeoToolsProcessors {

  public Function<Stream<GeodataRecord>, Stream<GeodataRecord>> reproject(
      @NonNull String targetSrs) {

    RecordReprojectFunction transformer = new RecordReprojectFunction(targetSrs);
    return stream -> stream.map(transformer);
  }


  public Function<Stream<Geometry>, Stream<Geometry>> reproject(@NonNull String sourceSrs,
      @NonNull String targetSrs) {

    GeometryReprojectFunction transformer = new GeometryReprojectFunction(targetSrs);
    return stream -> stream.map(g -> transformer.apply(g, sourceSrs));
  }
}
