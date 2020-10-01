/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.fao.geonet.indexing.controller;

import java.util.Locale;
import lombok.val;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "index")
public class IndexController {

  @Autowired
  MessageSource messages;

  /**
   * Index all records.
   */
  @GetMapping(path = "/all")
  public ResponseEntity indexAll(
      @RequestHeader(value = "Accept-Language", required = false)
          String locale
  ) {
    String message = messages
        .getMessage("index.status.available", null, Locale.forLanguageTag(locale));

    return ResponseEntity.ok().build();
  }
}
