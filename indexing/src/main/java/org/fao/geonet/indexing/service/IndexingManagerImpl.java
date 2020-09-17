package org.fao.geonet.indexing.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import org.fao.geonet.domain.Metadata;
import org.fao.geonet.indexing.model.IndexingTask;
import org.fao.geonet.indexing.model.IndexingTaskImpl;
import org.fao.geonet.indexing.model.IndexingTaskMetricsDecorator;
import org.fao.geonet.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IndexingManagerImpl implements IndexingManager {

  @Autowired
  MetadataRepository metadataRepository;
  MeterRegistry meterRegistry;
  Counter totalNumberOfRecordsIndexedCounter;
  AtomicLong totalNumberOfRecordsToIndex = new AtomicLong(0);
  @Autowired
  private EntityManager entityManager;

  IndexingManagerImpl(MeterRegistry meterRegistry) {
    this.meterRegistry = meterRegistry;
    totalNumberOfRecordsIndexedCounter = Counter.builder("gn.index.totalNumberOfRecordsIndexed")
        .baseUnit("records")
        .description("Total number of records to index (in all tasks).")
        .tags("gn", "index")
        .register(meterRegistry);
    totalNumberOfRecordsToIndex = meterRegistry
        .gauge("gn.index.totalNumberOfRecordsToIndex", new AtomicLong(0));
  }

  @Override
  @Transactional(readOnly = true)
  public void indexAll() {
    long numberOfDbRecordsToIndex = metadataRepository.count();
    IndexingTask indexingTask = new IndexingTaskMetricsDecorator(
        new IndexingTaskImpl(numberOfDbRecordsToIndex),
        meterRegistry);

    System.out.println(numberOfDbRecordsToIndex);

    totalNumberOfRecordsToIndex.addAndGet(numberOfDbRecordsToIndex);

    Stream<Metadata> dbRecords = metadataRepository.stream(
        null,
        Metadata.class);

    dbRecords
        .peek(record -> indexRecord(record, indexingTask))
        .forEach(entityManager::detach);

    // TODO Clear metrics at some point? eg. 10 min after the end of the task
  }

  private void indexRecord(Metadata dbRecord, IndexingTask indexingTask) {
    System.out.println(String.format(
        "Indexing record: %s. Gauge %d",
        dbRecord.getUuid(), totalNumberOfRecordsToIndex.get()));
    indexingTask.index(dbRecord);
    totalNumberOfRecordsIndexedCounter.increment();
    totalNumberOfRecordsToIndex.decrementAndGet();
  }
}
