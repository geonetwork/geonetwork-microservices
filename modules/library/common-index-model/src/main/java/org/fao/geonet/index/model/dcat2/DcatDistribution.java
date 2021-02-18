/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.dcat2;


import static org.fao.geonet.index.model.dcat2.Namespaces.ADMS_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.DCATAP_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.DCAT_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.DCT_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.FOAF_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.RDFS_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.SPDX_URI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * A physical embodiment of the Dataset in a particular format.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DcatDistribution {

  @NonNull
  @XmlElement(name = "accessURL", namespace = DCAT_URI)
  String accessUrl;

  @XmlElement(name = "downloadURL", namespace = DCAT_URI)
  List<String> downloadUrl = new ArrayList();

  @XmlElement(namespace = DCT_URI)
  List<String> title = new ArrayList();

  @XmlElement(namespace = DCT_URI)
  List<String> description = new ArrayList();

  @XmlElement(namespace = ADMS_URI)
  Subject representationTechnique;

  @XmlElement(namespace = ADMS_URI)
  Subject status;

  /**
   * This property indicates how long it is planned to keep the Distribution of the Dataset
   * available.
   */
  @XmlElement(namespace = DCATAP_URI)
  List<Subject> availability = new ArrayList<>();

  @XmlElement(namespace = DCAT_URI)
  BigInteger byteSize;

  @XmlElement(namespace = DCAT_URI)
  List<RdfResource> compressFormat = new ArrayList();

  /**
   * A value that allows the contents of a file to be authenticated. This class allows the results
   * of a variety of checksum and cryptographic message digest algorithms to be represented.
   */
  @XmlElement(namespace = SPDX_URI)
  String checksum;

  // TODO
  /**
   * A media type, e.g. the format of a computer file.
   */
  @XmlElement(namespace = DCAT_URI)
  String mediaType;

  // TODO
  @XmlElement(namespace = DCAT_URI)
  List<String> packageFormat = new ArrayList();

  // TODO
  @XmlElement(namespace = DCAT_URI)
  List<RdfResource> format = new ArrayList();


  @XmlElement(namespace = DCAT_URI)
  List<BigDecimal> spatialResolutionInMeters = new ArrayList();

  // TODO: Adapter
  @XmlElement(namespace = DCAT_URI)
  List<Duration> temporalResolution = new ArrayList();


  @XmlElement(namespace = DCAT_URI)
  List<DcatAccessRights> accessRights = new ArrayList();


  @XmlElement(namespace = DCT_URI)
  RdfResource conformsTo;

  @XmlElement(namespace = DCT_URI)
  Date issued;

  @XmlElement(namespace = DCT_URI)
  Date modified;

  @XmlElement(namespace = DCT_URI)
  List<RdfResource> language = new ArrayList<>();

  @XmlElement(namespace = DCAT_URI)
  List<DcatAccessRights> rights = new ArrayList();

  @XmlElement(namespace = RDFS_URI)
  List<String> comment = new ArrayList();

  // TODO
  //  @XmlElement(namespace = DQV_URI)
  //  QualityMeasurement hasQualityMeasurement = new ArrayList();

  @XmlElement(namespace = FOAF_URI)
  List<DcatDocument> page;
}
