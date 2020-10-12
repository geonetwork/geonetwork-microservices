/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.indexing.exception;

public class IndexingRecordException extends Exception {
  public IndexingRecordException(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }

  public IndexingRecordException(String errorMessage) {
    super(errorMessage);
  }
}
