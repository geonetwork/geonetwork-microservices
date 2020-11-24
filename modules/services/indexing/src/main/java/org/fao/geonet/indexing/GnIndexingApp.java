/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.indexing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.fao.geonet")
public class GnIndexingApp {

  /**
   * Application launcher.
   */
  public static void main(String[] args) {
    try {
      SpringApplication.run(GnIndexingApp.class, args);
    } catch (RuntimeException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }
}
