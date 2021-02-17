package org.fao.geonet.index.model.dcat2;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import static org.fao.geonet.index.model.dcat2.Namespaces.DCT_URI;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Data
public class DcatLicenseDocumentContainer extends RdfResource {

  @XmlElement(name = "LicenseDocument", namespace = DCT_URI)
  DcatLicenseDocument license;

  @Builder
  public DcatLicenseDocumentContainer(String about, String resource, DcatLicenseDocument license) {
    super(about, resource);
    this.license = license;
  }
}
