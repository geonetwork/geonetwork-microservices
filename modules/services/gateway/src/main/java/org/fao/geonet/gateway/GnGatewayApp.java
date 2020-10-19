/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.gateway;

import java.net.URI;
import java.net.URISyntaxException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RefreshScope
public class GnGatewayApp {

  public static void main(String[] args) {
    SpringApplication.run(GnGatewayApp.class, args);
  }

  @Getter
  @Setter
  @Value("${gn.url:http://localhost:8080/geonetwork}")
  private String geonetworkUrl;

  /**
   * Register gateway routes.
   */
  @Bean
  public RouteLocator myRoutes(RouteLocatorBuilder builder) throws URISyntaxException {

    String geonetworkWebapp = new URI(geonetworkUrl).getPath();

    return builder.routes()
      .route("GeoNetwork route", p -> p
          .path(geonetworkWebapp + "/**")
          .uri(geonetworkUrl))
      .route(p -> p
        .path("/authenticate")
        .uri("http://127.0.0.1:9998/authenticate"))
      .route(p -> p
        .path("/search")
        .uri("http://127.0.0.1:9990/search"))
      .build();
  }
}
