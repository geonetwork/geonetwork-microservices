package org.fao.geonet.dataviz.processor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessorsAutoConfiguration {

  public @Bean Processors processors() {
    return new Processors();
  }
}
