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
public class DcatDistributionContainer extends RdfResource {

  @XmlElement(name = "Distribution", namespace = PROV_URI)
  DcatDistribution distribution;

  @Builder
  public DcatDistributionContainer(String about, String resource, DcatDistribution distribution) {
    super(about, resource);
    this.distribution = distribution;
  }
}
