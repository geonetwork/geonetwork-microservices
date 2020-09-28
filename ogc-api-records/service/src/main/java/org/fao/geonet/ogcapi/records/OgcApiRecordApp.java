package org.fao.geonet.ogcapi.records;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@RefreshScope
@Import({CapabilitiesApiController.class})
public class OgcApiRecordApp {
  public static void main(String[] args) {
    SpringApplication.run(OgcApiRecordApp.class, args);
  }
}
