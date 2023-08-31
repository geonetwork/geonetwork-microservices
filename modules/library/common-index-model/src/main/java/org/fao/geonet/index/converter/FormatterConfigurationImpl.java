package org.fao.geonet.index.converter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FormatterConfigurationImpl implements FormatterConfiguration {

  @Value("${gn.linkToLegacyGN4:false}")
  Boolean linkToLegacyGN4;

  /**
   * Used to enable customMetadataUrl in rss response.
   */
  @Value("${gn.linkToCustomMetadataUrl:false}")
  Boolean linkToCustomMetadataUrl;

  @Value("${gn.legacy.url}")
  String legacyUrl;

  /**
   * Used to override link to metadata in rss response. It takes precedence over legacyUrl if both enabled.
   * By default, the customMetadataUrl will redirect to host url with a trailing slash and followed by the metadata uuid. e.g: http://geonetwork.org/uuid
   * You can customize it by redirect to another service (always followed by metadata uuid). e.g: http://my-other-service.com/uuid
   */
  @Value("${gn.customMetadataUrl:}")
  String customMetadataUrl;

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
    if (linkToCustomMetadataUrl) {
      return customMetadataUrl;
    }
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
    if (linkToCustomMetadataUrl) {
      return String.format("%s/%s",
          customMetadataUrl,
          metadataId);
    }
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

  @Override
  public void setLinkToCustomUrl(Boolean linkToCustomMetadataUrl) {
    this.linkToCustomMetadataUrl = linkToCustomMetadataUrl;
  }
}
