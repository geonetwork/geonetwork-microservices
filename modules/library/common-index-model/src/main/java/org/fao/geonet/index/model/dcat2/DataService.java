/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.dcat2;

import static org.fao.geonet.index.model.dcat2.Namespaces.DCAT_URI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@XmlRootElement(namespace = DCAT_URI)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class DataService extends Resource {

  /**
   * The root location or primary endpoint of the service (a Web-resolvable IRI).
   */
  @XmlElement(name = "endpointURL", namespace = DCAT_URI)
  List<RdfResource> endpointUrl;

  /**
   * A collection of data that this data service can distribute.
   */
  @XmlElement(name = "servesDataset", namespace = DCAT_URI)
  List<RdfResource> servesDataset;
}
