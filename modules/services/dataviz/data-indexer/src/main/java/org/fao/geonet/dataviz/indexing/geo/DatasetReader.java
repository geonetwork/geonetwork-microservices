/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.indexing.geo;

import java.net.URI;
import lombok.NonNull;
import org.fao.geonet.dataviz.indexing.functions.GeodataRecord;
import reactor.core.publisher.Flux;

public interface DatasetReader {

  boolean canHandle(URI datasetUri);

  Flux<GeodataRecord> read(@NonNull URI datasetUri);

}
