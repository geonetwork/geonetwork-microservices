package org.fao.geonet.index.converter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FormatterConfigurationImpl implements FormatterConfiguration {

  @Value("${gn.linkToLegacyGN4:false}")
  Boolean linkToLegacyGN4;

  @Value("${gn.legacy.url}")
  String legacyUrl;

  @Value("${gn.baseurl}")
  private String baseUrl;

  /** historically built from settings system/site/name. */
  @Value("${gn.site.name:GeoNetwork}")
  String siteName;

  /** historically built from settings system/site/organization. */
  @Value("${gn.site.organization:opensource}")
  String siteOrg;

  @Override
  public String getSourceHomePage() {
    if (linkToLegacyGN4) {
      return legacyUrl;
    }
    return baseUrl;
  }

  @Override
  public String getSourceSiteTitle() {
    return String.format("%s %s",
            siteName,
            siteOrg);
  }

  @Override
  public String buildLandingPageLink(String metadataId) {
    if (linkToLegacyGN4) {
      return String.format("%s/srv/metadata/%s",
              legacyUrl,
              metadataId);
    }
    return String.format("%s/collections/%s/items/%s",
            baseUrl,
            "main",
            metadataId);
  }

  @Override
  public void setLinkToLegacyGN4(Boolean linkToLegacyGN4) {
    this.linkToLegacyGN4 = linkToLegacyGN4;
  }
}
