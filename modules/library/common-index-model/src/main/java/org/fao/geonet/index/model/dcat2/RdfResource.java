package org.fao.geonet.index.model.dcat2;

import static org.fao.geonet.index.model.dcat2.Namespaces.RDF_URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RdfResource {

  @XmlAttribute(namespace = RDF_URI)
  String about;
  @XmlAttribute(namespace = RDF_URI)
  String resource;

}
