package org.fao.geonet.dataviz.sink;

import java.util.function.Consumer;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.dataviz.model.GeodataRecord;
import reactor.core.publisher.Flux;

@Slf4j
public class Consumers {

  public void index(@NonNull Flux<GeodataRecord> records) {
    Consumer<? super GeodataRecord> consumer = rec -> {
//      log.info("processing record {}", rec.getId());
    };

    Consumer<? super Throwable> errorConsumer = err -> {
      log.warn("Error processing records", err);
    };
    Runnable completeConsumer = () -> {
      log.info("Record indexing finished successfully");
    };
    
    records.subscribe(consumer, errorConsumer, completeConsumer);
  }

}
