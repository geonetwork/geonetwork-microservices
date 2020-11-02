/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.searching;


import org.fao.geonet.repository.GeonetRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@RefreshScope
@EntityScan(basePackages = { "org.fao.geonet.domain" })
@EnableJpaRepositories(
    basePackages = "org.fao.geonet.repository",
    repositoryBaseClass = GeonetRepositoryImpl.class)
@ComponentScan({"org.fao.geonet"})
public class GnSearchApp {

  public static void main(String[] args) {
    SpringApplication.run(GnSearchApp.class, args);
  }
}
