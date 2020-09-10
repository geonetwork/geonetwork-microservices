package org.fao.geonet.indexing.service;

import org.fao.geonet.indexing.model.IndexingTask;

import java.util.ArrayList;
import java.util.List;

public interface IndexingManager {
    List<IndexingTask> indexingTasks = new ArrayList<>();

    /**
     * Index all documents of the data source
     */
    void indexAll();
}
