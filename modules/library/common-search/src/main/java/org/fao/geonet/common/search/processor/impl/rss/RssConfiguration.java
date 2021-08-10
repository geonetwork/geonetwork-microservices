package org.fao.geonet.common.search.processor.impl.rss;

public interface RssConfiguration {

  /** link historically built from settings (protocol + server + port) and context (getBaseUrl). */
  String getLegacyUrl();

}
