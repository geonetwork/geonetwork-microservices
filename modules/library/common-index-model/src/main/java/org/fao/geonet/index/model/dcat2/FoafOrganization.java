/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.dcat2;

import static org.fao.geonet.index.model.dcat2.Namespaces.FOAF_URI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@XmlRootElement(name = "Organization", namespace = FOAF_URI)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoafOrganization extends RdfResource {

  @NonNull
  @XmlElement(namespace = FOAF_URI)
  String name;

  @XmlElement(namespace = FOAF_URI)
  RdfResource homepage;

  @XmlElement(namespace = FOAF_URI)
  List<FoafMember> member;

  //
  //locn:Address

  /**
   * Foaf agent.
   */
  @Builder
  public FoafOrganization(String about, String resource, String name) {
    super(about, resource, null);
    this.name = name;
    this.type = type;
  }
}
