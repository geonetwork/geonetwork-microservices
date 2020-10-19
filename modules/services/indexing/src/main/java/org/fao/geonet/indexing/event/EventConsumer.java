/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.indexing.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.ServiceMatcher;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * Sample {@link EventListener} for {@link IndexEvent}
 * {@link RemoteApplicationEvent remote application events}.
 */
@Slf4j
@Configuration
public class EventConsumer {

  private @Autowired ServiceMatcher busServiceMatcher;

  /**
   * Consume an event.
   */
  @EventListener(IndexEvent.class)
  public void consumeEvent(IndexEvent indexEvent) {
    if (busServiceMatcher.isFromSelf(indexEvent)) {
      log.info("Ignoring event from self {}", indexEvent);
    } else {
      log.info("Inbound message--> {}", indexEvent);
    }
  }
}