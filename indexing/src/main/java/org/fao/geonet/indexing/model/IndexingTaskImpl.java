package org.fao.geonet.indexing.model;

import io.micrometer.core.annotation.Timed;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.fao.geonet.domain.Metadata;

@ToString
public class IndexingTaskImpl implements IndexingTask {

  @Getter
  @Setter
  private String uuid;
  @Getter
  @Setter
  private String label;
  @Getter
  private LocalDateTime startDateTime;
  @Getter
  @Setter
  private LocalDateTime endDateTime;
  @Getter
  @Setter
  private IndexingTaskStatus status;
  @Getter
  @Setter
  private long size;
  @Getter
  private long current = 0;
  private Map<String, String> errors = new HashMap();

  /**
   * Indexing task.
   */
  public IndexingTaskImpl(Long size) {
    this.uuid = UUID.randomUUID().toString();
    this.startDateTime = LocalDateTime.now();
    this.status = IndexingTaskStatus.PENDING;
    this.size = size;
  }

  @Override
  public void reportError(String uuid, String message) {
    errors.put(uuid, message);
  }

  @Override
  @Timed(value = "gn.index.recordIndexingTime",
      description = "Time to index a record")
  public void index(Metadata dbRecord) {
    System.out.println(String.format(
        "Indexing record %s in thread %s",
        dbRecord.getUuid(),
        Thread.currentThread().getName()));
    try {
      Thread.sleep(300);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  //  public void increment() {
  //    if (current == 0) {
  //      this.status = IndexingTaskStatus.RUNNING;
  //    }
  //    if (current == size) {
  //      this.status = IndexingTaskStatus.COMPLETED;
  //    }
  //    current++;
  //  }
}
