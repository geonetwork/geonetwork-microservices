package org.fao.geonet.indexing.event;

import lombok.Data;

/**
 * Indexing event is used to add elements to the indexing queue.
 *
 * <p>
 * Can contains:
 * <ul>
 *   <li>A list of UUIDs</li>
 *   <li>A bucket identifier</li>
 *   <li>if none of them, will trigger a complete indexing task.</li>
 * </ul>
 * </p>
 */
@Data
public class IndexEvent {

  String[] uuid;
  String bucket;

  public IndexEvent() {
  }
}
