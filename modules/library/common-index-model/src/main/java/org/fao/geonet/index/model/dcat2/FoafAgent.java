package org.fao.geonet.index.model.dcat2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import static org.fao.geonet.index.model.dcat2.Namespaces.*;

@XmlRootElement(name = "Agent", namespace = FOAF_URI)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoafAgent extends RdfResource {

  @XmlElement(namespace = DCT_URI)
  Subject type;

  @NonNull
  @XmlElement(namespace = FOAF_URI)
  String name;

  //
  //locn:Address

  @Builder
  public FoafAgent(String about, String resource, String name) {
    super(about, resource);
    this.name = name;
    this.type = type;
  }
}
