package org.fao.geonet.dataviz.producer;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.fao.geonet.dataviz.model.DataQuery;
import org.fao.geonet.dataviz.model.GeodataRecord;

public class Producers {

  private @Getter @Setter List<DatasetReader> readers;

  public Producers() {
    this.readers = new ArrayList<>();
  }

  public Producers(List<DatasetReader> readers) {
    this.readers = new ArrayList<>(readers);
  }

  /**
   * 
   * @param query
   * @return
   * @throws IllegalArgumentException if no {@link DatasetReader reader} can
   *                                  handle the query
   */
  public Supplier<Stream<? extends GeodataRecord>> read(@NonNull DataQuery query) {
    final URI uri = query.getSource().getUri();
    Objects.requireNonNull(uri);

    DatasetReader reader = reader(uri).orElseThrow(noReaderException(uri));
    return () -> reader.read(query);
  }

  public Optional<DatasetReader> reader(@NonNull URI uri) {
    return readers.stream().filter(r -> r.canHandle(uri)).findFirst();
  }

  private Supplier<? extends IllegalArgumentException> noReaderException(URI uri) {
    return () -> new IllegalArgumentException(
        String.format("No DataReader can handle the provided URI %s. Available readers: '%s'", uri,
            readers.stream().map(DatasetReader::getName).collect(Collectors.joining(","))));
  }
}
