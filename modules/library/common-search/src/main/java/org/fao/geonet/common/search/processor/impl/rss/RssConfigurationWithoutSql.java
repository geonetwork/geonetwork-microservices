package org.fao.geonet.common.search.processor.impl.rss;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RssConfigurationWithoutSql implements RssConfiguration {

  @Value("${gn.legacy.url}")
  String legacyUrl;


  @Override
  public String getLegacyUrl() {
    return legacyUrl;
  }
}
