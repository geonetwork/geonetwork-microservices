package org.fao.geonet.dataviz.sink;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsumersAutoConfiguration {

  public @Bean Consumers consumers() {
    return new Consumers();
  }
}
