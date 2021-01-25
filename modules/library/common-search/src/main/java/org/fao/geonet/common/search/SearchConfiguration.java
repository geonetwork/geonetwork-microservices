/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.common.search;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "gn.search")
public class SearchConfiguration {
  String defaultMimeType;

  List<String> sortables = new ArrayList<>();

  List<String> sources = new ArrayList<>();

  List<Format> formats = new ArrayList<>();

  @Data
  public static class Format {
    String name;
    String mimeType;
    String responseProcessor;
  }
}
