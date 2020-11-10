/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.indexing.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.BusProperties;
import org.springframework.cloud.bus.ConditionalOnBusEnabled;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class EventStreamService {

  private static final String DESTINATION_ALL_SERVICES = null;

  private @Autowired ApplicationEventPublisher eventPublisher;

  private @Autowired BusProperties busProperties;

  /**
   * Publishes an {@link IndexEvent}. Being a {@link RemoteApplicationEvent}, if
   * spring-bus {@link ConditionalOnBusEnabled is enabled}, it will automatically
   * be distributed to all nodes in the cluster.
   *
   */
  public void produceEvent(String bucket, String uuid) {
    System.out.println("Producing events --> uuid: " + uuid + " bucket: " + bucket);
    String originService = busProperties.getId();
    IndexEvent event = new IndexEvent(this, originService, DESTINATION_ALL_SERVICES);
    event.setBucket(bucket);
    event.setUuid(uuid);
    eventPublisher.publishEvent(event);
  }
}