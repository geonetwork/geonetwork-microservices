package org.fao.geonet.index.converter;

public interface FormatterConfiguration {

  /** link historically built from settings (protocol + server + port) and context (getBaseUrl). */
  String getSourceHomePage();

  /** historically built from settings system/site/name. */
  String getSiteName();

  /** historically built from settings system/site/organization. */
  String getSiteOrganization();

  String buildLandingPageLink(String metadataId);

  /** for testing purposes. */
  void setLinkToLegacyGN4(Boolean linkToLegacyGN4);
}
