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

/**
 * A responsibility of an Agent for a resource.
 *
 * <p>https://www.w3.org/TR/prov-o/#Attribution</p>
 */
@XmlRootElement(name = "Attribution", namespace = PROV_URI)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProvAttribution {

  @XmlElement(namespace = DCAT_URI)
  RdfResource hadRole;

  // TODO: Needs improvements prov:agent
  // https://github.com/geonetwork/geonetwork-microservices/blob/main/modules/services/ogc-api-records/service/src/main/resources/xslt/ogcapir/formats/dcat/dcat-iso19139.xsl#L1808
  @XmlElement(namespace = PROV_URI)
  RdfResource agent;
}
