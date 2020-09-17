package org.fao.geonet.indexing.model;

import org.fao.geonet.domain.Metadata;

public interface IndexingTask {

  void index(Metadata dbRecord);

  void reportError(String uuid, String message);
}
