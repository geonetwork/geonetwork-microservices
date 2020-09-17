package org.fao.geonet.indexing.controller;

import java.util.Locale;
import lombok.val;
import org.fao.geonet.indexing.service.IndexingManagerImpl;
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
  IndexingManagerImpl indexingManager;

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
    val message = messages
        .getMessage("index.status.available", null, Locale.forLanguageTag(locale));

    indexingManager.indexAll();

    return ResponseEntity.ok().build();
  }
}
