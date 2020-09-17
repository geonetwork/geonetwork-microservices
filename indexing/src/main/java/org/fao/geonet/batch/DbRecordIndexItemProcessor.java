package org.fao.geonet.batch;

import org.fao.geonet.domain.Metadata;
import org.fao.geonet.indexing.model.IndexRecord;
import org.springframework.batch.item.ItemProcessor;

public class DbRecordIndexItemProcessor
    implements ItemProcessor<Metadata, IndexRecord> {

  @Override
  public IndexRecord process(Metadata metadata) throws Exception {
    System.out.println(String.format(
        "Processing %s in thread %s",
        metadata.getUuid(),
        Thread.currentThread().getName()));
    return new IndexRecord(metadata);
  }
}