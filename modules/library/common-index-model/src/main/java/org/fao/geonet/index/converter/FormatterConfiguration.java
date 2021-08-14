package org.fao.geonet.index.converter;

public interface FormatterConfiguration {

  /** link historically built from settings (protocol + server + port) and context (getBaseUrl). */
  String getSourceHomePage();

  String getSourceSiteTitle();

  String buildLandingPageLink(String metadataId);

  /** for testing purposes. */
  void setLinkToLegacyGN4(Boolean linkToLegacyGN4);
}
