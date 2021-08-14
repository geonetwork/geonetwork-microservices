package org.fao.geonet.index.converter.rss;

public interface RssConfiguration {

  /** link historically built from settings (protocol + server + port) and context (getBaseUrl). */
  String getLegacyUrl();

  /** historically built from settings system/site/name. */
  String getSiteName();

  /** historically built from settings system/site/organization. */
  String getSiteOrganization();

}
