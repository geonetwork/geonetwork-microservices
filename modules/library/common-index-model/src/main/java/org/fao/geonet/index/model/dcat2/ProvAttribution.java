/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.index.model.dcat2;

import static org.fao.geonet.index.model.dcat2.Namespaces.DCAT_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.PROV_URI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
