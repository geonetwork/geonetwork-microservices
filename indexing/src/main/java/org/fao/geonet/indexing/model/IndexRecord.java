/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.indexing.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.fao.geonet.domain.Metadata;


@XmlRootElement(name = "indexRecord")
@Data
public class IndexRecord extends IndexDocument {
  private String docType;
  private String indexingDate;
  private String documentStandard;
  private String standardName;
  private String metadataIdentifier;
  private String mainLanguage;
  private List<String> otherLanguages = new ArrayList<>();
  Map<String, String> resourceTitleObject = new HashMap<>();
  private String resourceAbstract = "";
  //  private List<Link> resourceLinks;

  //  @JsonAnyGetter
  private Map<String, String> otherProperties = new HashMap();

  /**
   * Record to be loaded in the index.
   */
  public IndexRecord(Metadata dbRecord) {
    this.documentStandard = "";
    this.metadataIdentifier = "";
    // Use XSLT or Java to extract properties
  }

  public IndexRecord() {
  }
}
