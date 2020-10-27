/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.searching;

import org.fao.geonet.searching.controller.HelloController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@RefreshScope
@Import({HelloController.class})
public class GnSearchApp {

  public static void main(String[] args) {
    SpringApplication.run(GnSearchApp.class, args);
  }
}
