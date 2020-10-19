package org.fao.geonet.indexing.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;

/**
 * Indexing event is used to add elements to the indexing queue.
 *
 * <p>
 * Can contains:
 * <ul>
 * <li>A list of UUIDs</li>
 * <li>A bucket identifier</li>
 * <li>if none of them, will trigger a complete indexing task.</li>
 * </ul>
 * </p>
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class IndexEvent extends RemoteApplicationEvent {
  private static final long serialVersionUID = 862037593554999167L;

  private @Getter @Setter String uuid;
  private @Getter @Setter String bucket;

  /**
   * Default constructor for de-serialization.
   */
  public IndexEvent() {
    super();
  }

  public IndexEvent(Object source, String originService, String destinationService) {
    super(source, originService, destinationService);
  }
}
