/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.dcat2;

import static org.fao.geonet.index.model.dcat2.Namespaces.DCT_URI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Data
public class DcatLicenseDocumentContainer extends RdfResource {

  @XmlElement(name = "LicenseDocument", namespace = DCT_URI)
  DcatLicenseDocument license;

  @Builder
  public DcatLicenseDocumentContainer(String about, String resource, DcatLicenseDocument license) {
    super(about, resource, null);
    this.license = license;
  }
}
