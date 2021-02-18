/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.dcat2;

import static org.fao.geonet.index.model.dcat2.Namespaces.DCT_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.FOAF_URI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A textual resource intended for human consumption that contains information, e.g., a Web page
 * about a Dataset.
 */
@XmlRootElement(name = "Document", namespace = FOAF_URI)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoafDocument extends RdfResource {

  @XmlElement(namespace = DCT_URI)
  String title;

  @XmlElement(namespace = DCT_URI)
  String description;

  @XmlElement(namespace = DCT_URI)
  String format;

  /**
   * Foaf document.
   */
  @Builder
  public FoafDocument(String about, String resource, String title, String description,
      String format) {
    super(about, resource, null);
    this.title = title;
    this.description = description;
    this.format = format;
  }
}
