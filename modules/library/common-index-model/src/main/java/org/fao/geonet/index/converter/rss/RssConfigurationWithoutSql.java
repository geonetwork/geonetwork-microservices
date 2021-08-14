package org.fao.geonet.index.converter.rss;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RssConfigurationWithoutSql implements RssConfiguration {

  @Value("${gn.legacy.url}")
  String legacyUrl;

  @Value("${gn.site.name:GeoNetwork}")
  String siteName;

  @Value("${gn.site.organization:opensource}")
  String siteOrg;

  @Override
  public String getLegacyUrl() {
    return legacyUrl;
  }

  @Override
  public String getSiteName() {
    return siteName;
  }

  @Override
  public String getSiteOrganization() {
    return siteOrg;
  }
}
