package org.fao.geonet.dataviz.producer;

import java.net.URI;
import java.util.List;
import lombok.NonNull;
import org.fao.geonet.dataviz.model.GeodataRecord;
import org.fao.geonet.dataviz.model.DataQuery;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Producers {

  private @Autowired List<DatasetReader> readerImpls;

  public Flux<GeodataRecord> read(@NonNull DataQuery query) {
    Mono<DatasetReader> reader = Mono.just(query.getUri()).flatMap(this::reader);
    return reader.flatMapMany(q -> q.read(query));
  }

  public Mono<DatasetReader> reader(@NonNull URI uri) {
    return Mono.justOrEmpty(readerImpls.stream().filter(r -> r.canHandle(uri)).findFirst());
  }
}
