package org.fao.geonet.dataviz.processor;

import org.fao.geonet.dataviz.processor.geotools.GeoToolsProcessors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessorsAutoConfiguration {

  public @Bean Processors processors() {
    return new Processors();
  }

  public @Bean StandardProcessors standardProcessors() {
    return new StandardProcessors();
  }

  public @Bean GeoToolsProcessors geoToolsProcessors() {
    return new GeoToolsProcessors();
  }
}
