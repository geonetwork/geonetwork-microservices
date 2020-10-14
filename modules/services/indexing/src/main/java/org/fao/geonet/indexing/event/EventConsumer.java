/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.indexing.event;


import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Payload;

@Configuration
public class EventConsumer {

  /**
   * Consume an event.
   */
  @StreamListener(EventStream.INBOUND)
  public void consumeEvent(@Payload IndexEvent indexEvent) {
    System.out.println(
        "Inbound message--> uuid: " + indexEvent.getUuid()
            + " bucket: " + indexEvent.getBucket());
  }
}