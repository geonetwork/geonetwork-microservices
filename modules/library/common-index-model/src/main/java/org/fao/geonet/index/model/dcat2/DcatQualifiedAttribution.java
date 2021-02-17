package org.fao.geonet.index.model.dcat2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import static org.fao.geonet.index.model.dcat2.Namespaces.PROV_URI;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Data
@Builder
public class DcatQualifiedAttribution {

  @XmlElement(name = "Attribution", namespace = PROV_URI)
  ProvAttribution attribution;
}
