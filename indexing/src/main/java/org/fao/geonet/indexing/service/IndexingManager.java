package org.fao.geonet.indexing.service;

import java.util.ArrayList;
import java.util.List;
import org.fao.geonet.indexing.model.IndexingTask;

public interface IndexingManager {

  List<IndexingTask> indexingTasks = new ArrayList<>();

  /**
   * Index all documents of the data source.
   */
  void indexAll();
}
