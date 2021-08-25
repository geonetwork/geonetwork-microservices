/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.indexing;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.dataviz.model.AuthCredentials;
import org.fao.geonet.dataviz.model.AuthCredentials.AuthType;
import org.fao.geonet.dataviz.model.DataQuery;
import org.fao.geonet.dataviz.model.DataSource;
import org.fao.geonet.dataviz.model.GeodataRecord;
import org.fao.geonet.dataviz.processor.Processors;
import org.fao.geonet.dataviz.producer.Producers;
import org.fao.geonet.dataviz.sink.Consumers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
@SpringBootApplication
public class DatavizIndexingApp {

  private @Autowired Producers producers;
  private @Autowired Processors processors;
  private @Autowired Consumers consumers;

  /**
   * Application launcher.
   */
  public static void main(String[] args) {
    try {
      SpringApplication.run(DatavizIndexingApp.class, args);
    } catch (RuntimeException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  @Bean
  public Supplier<DataQuery> sampleQuery() {

    DataSource source = new DataSource()
        .setUri(URI.create("http://ows.example.com/wfs?request=GetCapabilities"))
        .setEncoding(StandardCharsets.UTF_8).setAuth(new AuthCredentials().setUserName("user")
            .setPassword("secret").setType(AuthType.basic));

    return () -> new DataQuery().setSource(source).setLayerName("topp:states");
  }

  @Bean
  public Function<Flux<URI>, Flux<GeodataRecord>> indexAll() {
    // Consumer<GeodataRecord> c;
    Function<Flux<URI>, Flux<GeodataRecord>> source = readAll().andThen(toWgs84());
    return source;
  }

  @Bean
  public Consumer<Flux<GeodataRecord>> index() {
    return records -> {
      log.info("index()");
      consumers.index(records.publishOn(Schedulers.parallel()));
    };
  }

  /**
   * A function that takes a dataset URI and returns a flux of
   * {@link GeodataRecord}.
   * 
   * <p>
   * Maps to HTTP as: {@code POST /read} with an {@link URI} as request body.
   * 
   * @return a function that takes a dataset URI and returns a flux of
   *         {@link GeodataRecord}
   */
  @Bean
  public Function<Flux<URI>, Flux<GeodataRecord>> readAll() {
    return uris -> uris.flatMap(uri -> toFlux(producers.read(DataQuery.fromUri(uri))));
  }

  @Bean
  public Function<Flux<DataQuery>, Flux<GeodataRecord>> read() {
    return queries -> queries.flatMap(query -> {
      log.info("read() {}", query);
      return toFlux(producers.read(query));
    });
  }

  @Bean
  public Function<Flux<GeodataRecord>, Flux<GeodataRecord>> toWgs84() {
    return flux -> toFlux(() -> {
      log.info("toWgs84()");
      Stream<GeodataRecord> in = flux.toStream();
      Stream<GeodataRecord> o = processors.toWgs84().apply(in);
      return o;
    });
  }

  /**
   * Adapts a {@link Stream} to a {@link Flux}, note that
   * {@link Flux#fromStream(Stream)} already makes sure {@link Stream#close()} is
   * called when the stream is fully consumed.
   */
  private <T> Flux<T> toFlux(Supplier<Stream<? extends T>> stream) {
    return Flux.fromStream(stream)//
        // subscribe on Schedulers.boundedElastic() to avoid
        // "java.lang.IllegalStateException: Iterating over a toIterable() / toStream()
        // is blocking, which is not supported in thread reactor-http-epoll-XXX"
        .subscribeOn(Schedulers.boundedElastic());
  }
}
