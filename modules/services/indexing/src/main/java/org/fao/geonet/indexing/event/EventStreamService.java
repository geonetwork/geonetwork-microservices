/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.indexing.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

@Service
public class EventStreamService {

  @Autowired
  private EventStream eventStream;

  /**
   * Produce an indexing event.
   */
  public Boolean produceEvent(IndexEvent indexEvent) {
    System.out.println("Producing events --> uuid: "
        + indexEvent.getUuid()
        + " bucket: " + indexEvent.getBucket());
    MessageChannel messageChannel = eventStream.producer();
    return messageChannel.send(MessageBuilder.withPayload(indexEvent)
        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());

  }
}