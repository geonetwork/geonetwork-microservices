/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.producer;

import java.net.URI;
import lombok.NonNull;
import org.fao.geonet.dataviz.model.GeodataRecord;
import org.fao.geonet.dataviz.model.DataQuery;
import reactor.core.publisher.Flux;

public interface DatasetReader {

  boolean canHandle(URI datasetUri);

  Flux<GeodataRecord> read(@NonNull DataQuery query);

}
