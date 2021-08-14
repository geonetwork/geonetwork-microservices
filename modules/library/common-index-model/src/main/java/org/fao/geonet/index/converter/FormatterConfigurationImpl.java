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

  @Value("${gn.site.name:GeoNetwork}")
  String siteName;

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
  public String getSiteName() {
    return siteName;
  }

  @Override
  public String getSiteOrganization() {
    return siteOrg;
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
