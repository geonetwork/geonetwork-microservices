package org.fao.geonet.dataviz.sink;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.dataviz.model.GeodataRecord;
import reactor.core.publisher.Flux;

@Slf4j
public class Consumers {

  public void index(@NonNull Flux<GeodataRecord> records) {
    records.subscribe(rec -> {
      log.info("processing record {}", rec.getId());
    }, err -> {
      log.warn("Error processing records", err);
    }, () -> {
      log.info("Record indexing finished successfully");
    });
  }

}
