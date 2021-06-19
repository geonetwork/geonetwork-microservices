package org.fao.geonet.dataviz.processor;

import java.util.function.Function;
import lombok.NonNull;
import org.fao.geonet.dataviz.model.GeodataRecord;
import reactor.core.publisher.Flux;

public class Processors {

  public Function<Flux<GeodataRecord>, Flux<GeodataRecord>> reproject(@NonNull String targetSrs) {
    RecordReprojectFunction transformer = new RecordReprojectFunction(targetSrs);
    return flux -> flux.map(transformer);
  }
}
