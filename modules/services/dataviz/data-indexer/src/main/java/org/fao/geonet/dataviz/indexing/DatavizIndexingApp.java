/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.indexing;

import static java.util.Collections.singletonList;
import java.net.URI;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import org.fao.geonet.dataviz.indexing.functions.GeodataRecord;
import org.fao.geonet.dataviz.indexing.functions.GeometryProperty;
import org.fao.geonet.dataviz.indexing.functions.SimpleProperty;
import org.fao.geonet.dataviz.indexing.geo.DatasetReader;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
@ComponentScan
public class DatavizIndexingApp {

  private @Autowired List<DatasetReader> dataReaders;

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
  public Supplier<String> ping() {
    return () -> "pong";
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
  public Function<Flux<URI>, Flux<GeodataRecord>> read() {
    return uris -> uris.flatMap(uri -> Flux.just(//
        sample(uri.toString()), //
        sample(uri.toString()), //
        sample(uri.toString())//
    ));
  }

  private GeodataRecord sample(String name) {
    CoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
    Geometry g = new GeometryFactory(sf).createPoint(new CoordinateXY(-180, 90));
    GeometryProperty gprop = new GeometryProperty("geom", g, "EPSG:2958");
    return new GeodataRecord().setGeometry(gprop)
        .setProperties(singletonList(new SimpleProperty<>("uri", name)));
  }

  @Bean
  public Function<Flux<URI>, Flux<GeodataRecord>> index() {
    return read().andThen(toWgs84());
  }

  @Bean
  public Function<Flux<GeodataRecord>, Flux<GeodataRecord>> toWgs84() {
    return records -> records.map(rec -> {
      GeometryProperty g = rec.getGeometry();
      return rec
          .withGeometry(g == null ? null : g.withName(g.getName() + "_tx").withSrs("EPSG:4326"));
    });
  }

  private Mono<DatasetReader> findReader(URI uri) {
    return Flux.fromIterable(this.dataReaders).filter(reader -> reader.canHandle(uri)).single();
  }

}
