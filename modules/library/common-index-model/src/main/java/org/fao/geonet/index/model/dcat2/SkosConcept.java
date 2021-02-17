package org.fao.geonet.index.model.dcat2;

import static org.fao.geonet.index.model.dcat2.Namespaces.SKOS_URI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "Concept", namespace = SKOS_URI)
@XmlSeeAlso(RdfResource.class)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class SkosConcept extends RdfResource {

  @XmlElement(namespace = SKOS_URI)
  String prefLabel;

  @Builder
  public SkosConcept(String about, String resource, String prefLabel) {
    super(about, resource);
    this.prefLabel = prefLabel;
  }
}
