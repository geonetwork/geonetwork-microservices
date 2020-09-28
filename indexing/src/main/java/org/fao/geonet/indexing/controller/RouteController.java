package org.fao.geonet.indexing.controller;

import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "routes")
public class RouteController {

  @Autowired
  CamelContext camelContext;

  /**
   * Reload route configuration.
   */
  @GetMapping(path = "/reload")
  public ResponseEntity reload() {
    camelContext.stop();
    camelContext.start();
    return ResponseEntity.ok().build();
  }
}
