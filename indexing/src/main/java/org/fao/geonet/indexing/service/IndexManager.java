package org.fao.geonet.indexing.service;

public interface IndexManager {
    /**
     * Check if application indices exists
     * and create them if needed.
     */
    void init();

    /**
     * Create an index for the application.
     *
     * @param indexName Unique name of the index. eg. gn-records
     * @param dropIfExists Drop index first if exist.
     */
    void create(String indexName, boolean dropIfExists);

    /**
     * Compute and store index metrics.
     *
     * @param indexName
     */
    void computeMetrics(String indexName);

    /**
     * Delete an index.
     *
     * @param indexName
     */
    void delete(String indexName);

    /**
     * Remove all application indices.
     */
    void deleteAll();
}
