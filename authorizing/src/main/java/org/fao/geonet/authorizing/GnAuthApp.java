/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.authorizing;

import org.fao.geonet.common.JwtTokenUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@RefreshScope
@Import({SecurityConfigurer.class, JwtAuthenticationController.class, JwtTokenUtil.class})
public class GnAuthApp {

  public static void main(String[] args) {
    SpringApplication.run(GnAuthApp.class, args);
  }
}
