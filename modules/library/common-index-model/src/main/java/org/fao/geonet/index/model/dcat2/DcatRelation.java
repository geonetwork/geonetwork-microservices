package org.fao.geonet.index.model.dcat2;

import static org.fao.geonet.index.model.dcat2.Namespaces.DCAT_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.DCT_URI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "Relationship", namespace = DCAT_URI)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DcatRelation {

  // TODO: Needs improvements
  @XmlElement(namespace = DCT_URI)
  String relation;
  @XmlElement(namespace = DCAT_URI)
  String hadRole;
}
