/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.indexing.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

public class SendEventApp {

  @Value("${gn.indexing.supplier.enabled}")
  Boolean supplierEnabled = true;

  @Bean
  public void indexEventSupplier() {
    createEvent();
  }

  private Message<IndexEvent> createEvent() {
    return supplierEnabled
        ? MessageBuilder.withPayload(new IndexEvent())
        .setHeader("to_process", true)
        .build()
        : null;
  }
}
