/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.authorizing;

import org.fao.geonet.repository.GeonetRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@EntityScan(basePackages = { "org.fao.geonet.domain" })
@EnableJpaRepositories(
    basePackages = "org.fao.geonet.repository",
    repositoryBaseClass = GeonetRepositoryImpl.class)
@ComponentScan("org.fao.geonet")
@RefreshScope
public class GnAuthApp {

  @Bean
  public UserDetailsService userDetailsService() {
    return new GnUserDetailsService();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  public static void main(String[] args) {
    SpringApplication.run(GnAuthApp.class, args);
  }
}
