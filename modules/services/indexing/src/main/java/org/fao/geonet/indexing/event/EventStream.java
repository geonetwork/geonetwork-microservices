/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.indexing.event;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface EventStream {
  String INBOUND = "event-consumer";
  String OUTBOUND = "event-producer";

  @Input(INBOUND)
  SubscribableChannel consumer();

  @Output(OUTBOUND)
  MessageChannel producer();
}