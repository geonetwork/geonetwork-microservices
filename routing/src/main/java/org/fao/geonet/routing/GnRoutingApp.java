package org.fao.geonet.routing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RefreshScope
public class GnRoutingApp {

  public static void main(String[] args) {
    SpringApplication.run(GnRoutingApp.class, args);
  }

  /**
   * Register gateway routes.
   */
  @Bean
  public RouteLocator myRoutes(RouteLocatorBuilder builder) {
    return builder.routes()
      .route(p -> p
        .path("/authenticate")
        .uri("http://127.0.0.1:9998/authenticate"))
      .route(p -> p
        .path("/search")
        .uri("http://127.0.0.1:9990/search"))
      .build();
  }
}
