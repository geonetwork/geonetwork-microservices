/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.authorizing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;


@SpringBootApplication
@ComponentScan("org.fao.geonet")
@RefreshScope
public class GnAuthApp {

  @Bean
  public UserDetailsService userDetailsService() {
    return new GnUserDetailsService();
  }

  /**
   * GN4 password encoder.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new StandardPasswordEncoder("secret-hash-salt=");
    //    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    //    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  public static void main(String[] args) {
    SpringApplication.run(GnAuthApp.class, args);
  }
}
