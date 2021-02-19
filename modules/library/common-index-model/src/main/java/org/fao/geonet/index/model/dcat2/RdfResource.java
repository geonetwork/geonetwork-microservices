/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.dcat2;

import static org.fao.geonet.index.model.dcat2.Namespaces.DCT_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.RDF_URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"type", "title"})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RdfResource {

  @XmlAttribute(namespace = RDF_URI)
  String parseType;

  @XmlAttribute(namespace = RDF_URI)
  String about;

  @XmlAttribute(namespace = RDF_URI)
  String resource;

  @XmlElement(namespace = DCT_URI)
  String title;

  @XmlElement(namespace = RDF_URI)
  RdfType type;

  public RdfResource(String about, String resource) {
    this.about = about;
    this.resource = resource;
  }

  public RdfResource(String about, String resource, String title) {
    this(about, resource);
    this.title = title;
  }
}
