package org.fao.geonet.indexing.model;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.ToString;
import org.fao.geonet.domain.Metadata;

@ToString
public class IndexingTaskMetricsDecorator implements IndexingTask {

  Counter numberOfRecordsIndexed;
  Counter numberOfRecordsWithErrors;
  private IndexingTask task;
  private String metricPrefix;

  /**
   * Task providing progress metrics.
   */
  public IndexingTaskMetricsDecorator(IndexingTask task,
      MeterRegistry meterRegistry) {
    this.task = task;
    if (task instanceof IndexingTaskImpl) {
      String uuid = ((IndexingTaskImpl) task).getUuid();
      metricPrefix = String.format(
          "gn.index.task.%s.", uuid
      );

      numberOfRecordsIndexed =
          Counter.builder(String.format("%s%s", metricPrefix, "numberOfRecordsIndexed"))
              .baseUnit("records")
              .description("Number of records indexed in task.")
              .tags("gn", "index")
              .register(meterRegistry);
      numberOfRecordsWithErrors =
          Counter.builder(String.format("%s%s", metricPrefix, "numberOfRecordsWithErrors"))
              .baseUnit("records")
              .description("Number of records with indexing errors in task.")
              .tags("gn", "index")
              .register(meterRegistry);
    }
  }

  @Override
  public void reportError(String uuid, String message) {
    numberOfRecordsWithErrors.increment();
    task.reportError(uuid, message);
  }

  @Override
  public void index(Metadata dbRecord) {
    numberOfRecordsIndexed.increment();
    task.index(dbRecord);
  }
}
