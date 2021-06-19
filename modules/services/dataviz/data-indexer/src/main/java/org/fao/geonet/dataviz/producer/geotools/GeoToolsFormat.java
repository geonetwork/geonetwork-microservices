package org.fao.geonet.dataviz.producer.geotools;

import static java.util.Spliterator.DISTINCT;
import static java.util.Spliterator.IMMUTABLE;
import static java.util.Spliterator.NONNULL;
import static java.util.Spliterators.spliteratorUnknownSize;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterators;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.dataviz.model.DataQuery;
import org.fao.geonet.dataviz.model.GeodataRecord;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.feature.FeatureReaderIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public abstract class GeoToolsFormat {
  public abstract boolean canHandle(@NonNull URI datasetUri);

  protected abstract Map<String, ?> toConnectionParams(@NonNull DataQuery query);

  public Flux<GeodataRecord> read(@NonNull DataQuery dataQuery) {
    return Mono.just(dataQuery)// lazy eval
        .subscribeOn(Schedulers.boundedElastic()) // run on I/O thread pool
        .flatMapMany(query -> {
          try {
            final DataStore dataStore = resolveDataStore(query);
            final List<String> typeNames = resolveTypeNames(dataStore, query.getLayerName());

            return Flux.fromStream(typeNames.stream())//
                .flatMapSequential(typeName -> readFeatureType(dataStore, typeName, query.getUri()))//
                .doFinally(signalTpe -> dataStore.dispose());
          } catch (IOException e) {
            throw new IllegalStateException(e);
          }
        });
  }

  protected Flux<GeodataRecord> readFeatureType(final @NonNull DataStore store,
      final @NonNull String typeName, @NonNull URI uri) {

    return Mono.just(typeName).flatMapMany(type -> {
      log.debug("Getting FeatureReader for {}", type);
      FeatureReader<SimpleFeatureType, SimpleFeature> reader;
      try {
        Query query = new Query(type);
        Stopwatch sw = Stopwatch.createStarted();
        reader = store.getFeatureReader(query, Transaction.AUTO_COMMIT);
        log.info("FeatureReader obtained for {} in {}", type, sw.stop());
      } catch (IOException e) {
        throw new IllegalStateException(e);
      }
      Flux<SimpleFeature> features = readerToFlux(reader, typeName, uri);
      Flux<GeodataRecord> records = features.map(new FeatureToRecord());
      return records;
    });
  }

  protected Flux<SimpleFeature> readerToFlux(FeatureReader<SimpleFeatureType, SimpleFeature> reader,
      @NonNull String typeName, @NonNull URI uri) {

    Iterator<SimpleFeature> asIterator = new FeatureReaderIterator<SimpleFeature>(reader);
    final int spliteratorType = DISTINCT | IMMUTABLE | NONNULL;
    Spliterator<SimpleFeature> asSpliterator = spliteratorUnknownSize(asIterator, spliteratorType);
    final boolean parallel = false;
    Stream<SimpleFeature> asStream = StreamSupport.stream(asSpliterator, parallel);
    Flux<SimpleFeature> asFlux = Flux.fromStream(asStream);
    return asFlux.doFinally(signalType -> close(reader, typeName, uri));
  }

  /**
   * @param reader   the reader to close
   * @param typeName just for logging, required because
   *                 {@link FeatureReader#getFeatureType()} returns {@code null},
   *                 which seems like a bug in geotools
   * @param uri      data source URI, for logging purposes
   */
  private void close(FeatureReader<? extends FeatureType, ? extends Feature> reader,
      @NonNull String typeName, @NonNull URI uri) {
    log.debug("Closing feature reader {} from {}", typeName, uri);
    try {
      reader.close();
    } catch (IOException e) {
      log.warn("Error closing feature reader {}, won't affect flux", typeName, e);
    }
  }

  protected List<String> resolveTypeNames(@NonNull DataStore dataStore, String typeName)
      throws IOException {
    log.debug("Querying available type names");
    List<String> typeNames = Arrays.asList(dataStore.getTypeNames());
    Stopwatch sw = Stopwatch.createStarted();
    if (typeName != null) {
      if (!typeNames.contains(typeName)) {
        throw new IllegalArgumentException(
            "Requested layer " + typeName + " does not exist: " + typeNames);
      }
      log.info("Resolved requested type name {} out of {} published in {}", typeName,
          typeNames.size(), sw.stop());
      return Collections.singletonList(typeName);
    }
    log.info("Resolved {} type names in {}: {}", typeNames.size(), sw.stop(),
        typeNames.stream().collect(Collectors.joining(",")));
    return typeNames;
  }

  protected DataStore resolveDataStore(@NonNull DataQuery query) throws IOException {
    final Map<String, ?> params = toConnectionParams(query);
    if (log.isDebugEnabled()) {
      log.debug("Resolving DataStore for {} with params {}", query.getUri(),
          params.keySet().stream().sorted().collect(Collectors.joining(",")));
    }

    Iterator<DataStoreFactorySpi> matchingFactories = Iterators
        .filter(DataStoreFinder.getAvailableDataStores(), fac -> fac.canProcess(params));
    String dataStoreDisplayName = null;
    if (matchingFactories.hasNext()) {
      dataStoreDisplayName = matchingFactories.next().getDisplayName();
    }

    Stopwatch sw = Stopwatch.createStarted();
    DataStore dataStore = DataStoreFinder.getDataStore(params);
    if (null == dataStore) {
      throw new IllegalStateException("Unable to create DataStore for " + query);
    }
    if (dataStoreDisplayName == null) {
      dataStoreDisplayName = dataStore.getClass().getSimpleName();
    }
    log.info("Got {} DataStore in {} for {}", dataStoreDisplayName, sw.stop(), query.getUri());
    return dataStore;
  }

  protected URL toURL(@NonNull URI datasetUri) {
    final String scheme = datasetUri.getScheme();
    try {
      if (null == scheme) {
        File file = new File(datasetUri.toString());
        return file.getAbsoluteFile().toURI().toURL();
      }
      if ("file".equals(scheme)) {
        File file = new File(datasetUri);
        return file.getAbsoluteFile().toURI().toURL();
      }
      return datasetUri.toURL();
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
