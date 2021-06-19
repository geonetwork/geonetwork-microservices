package org.fao.geonet.dataviz.producer.geotools;

import lombok.Data;

/**
 * Global WFS protocol preferences
 */
@Data
public class WfsFormatProperties {

  /**
   * Indicates that datastore should do its best to create features from the
   * provided data even if it does not accurately match the schema.
   * 
   * Errors will be logged but the parsing will continue if this is true.
   */
  boolean lenient = true;

  // @formatter:off
  /**
   * Optional boolean DataStore parameter acting as a hint for the HTTP protocol
   * to use preferably against the WFS instance, with the following semantics:
   * 
   * null: (not supplied): use "AUTO", let the DataStore decide. true: use HTTP
   * POST preferably. false: use HTTP GET preferably.
   * 
   * Default: null
   */
  // @formatter:on
  Boolean preferPost;

  /**
   * Optional integer DataStore parameter indicating a timeout in milliseconds for
   * the HTTP connections.
   */
  Integer timeout = 30_000;

  /**
   * Optional DataStore parameter indicating whether to set the "Accept: gzip"
   * encoding on the HTTP request headers sent to the server
   */
  boolean tryGzip = true;

  /** Whether to use connection pooling for http(s) requests */
  boolean httpConnectionPooling = true;

  /**
   * Size of the connection pool to use for http(s) requests. Only activated when
   * http-connection-pooling is true
   */
  int httpConnectionPoolSize = 6;
}
