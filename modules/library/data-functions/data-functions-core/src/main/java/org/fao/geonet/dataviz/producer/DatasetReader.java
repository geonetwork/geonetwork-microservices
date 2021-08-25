/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.producer;

import java.net.URI;
import java.util.stream.Stream;
import lombok.NonNull;
import org.fao.geonet.dataviz.model.DataQuery;
import org.fao.geonet.dataviz.model.GeodataRecord;

public interface DatasetReader {

  String getName();
  
  boolean canHandle(URI datasetUri);

  Stream<GeodataRecord> read(@NonNull DataQuery query);

}
