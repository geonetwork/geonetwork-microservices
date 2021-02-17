package org.fao.geonet.index.model.dcat2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import static org.fao.geonet.index.model.dcat2.Namespaces.*;

@XmlRootElement(name = "RightsStatement", namespace = DCT_URI)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RightsStatement extends RdfResource {
  @XmlElement(namespace = RDFS_URI)
  String label;

  @Builder
  public RightsStatement(String about, String resource, String label) {
    super(about, resource);
    this.label = label;
  }
}
