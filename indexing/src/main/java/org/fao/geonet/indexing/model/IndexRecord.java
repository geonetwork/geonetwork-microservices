package org.fao.geonet.indexing.model;

import java.util.Map;
import org.fao.geonet.domain.Metadata;

public class IndexRecord extends IndexDocument {

  private String resourceTitle;
  private String resourceAbstract;
  private Map<String, String> otherProperties;

  /**
   * Record to be loaded in the index.
   */
  public IndexRecord(Metadata dbRecord) {
    // Use XSLT or Java to extract properties
  }
}
