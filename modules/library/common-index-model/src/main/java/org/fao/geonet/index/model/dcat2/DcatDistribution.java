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

  /**
   * A URL of the resource that gives access to a distribution of the dataset. E.g. landing page,
   * feed, SPARQL endpoint.
   */
  @NonNull
  @XmlElement(name = "accessURL", namespace = DCAT_URI)
  RdfResource accessUrl;

  /**
   * The URL of the downloadable file in a given format. E.g. CSV file or RDF file. The format is
   * indicated by the distribution's dct:format and/or dcat:mediaType
   */
  @XmlElement(name = "downloadURL", namespace = DCAT_URI)
  List<String> downloadUrl = new ArrayList<>();

  /**
   * A data service that gives access to the distribution of the dataset.
   */
  @XmlElement(name = "accessService", namespace = DCAT_URI)
  List<RdfResource> accessService = new ArrayList<>();

  /**
   * A name given to the distribution.
   */
  @XmlElement(namespace = DCT_URI)
  List<String> title = new ArrayList<>();

  /**
   * A free-text account of the distribution.
   */
  @XmlElement(namespace = DCT_URI)
  List<String> description = new ArrayList<>();

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
  List<RdfResource> compressFormat = new ArrayList<>();

  /**
   * A value that allows the contents of a file to be authenticated. This class allows the results
   * of a variety of checksum and cryptographic message digest algorithms to be represented.
   */
  @XmlElement(namespace = SPDX_URI)
  String checksum;

  // TODO
  /**
   * A media type, e.g. the format of a computer file.
   *
   * <p>The media type of the distribution as defined by IANA [IANA-MEDIA-TYPES]. This property
   * SHOULD be used when the media type of the distribution is defined in IANA [IANA-MEDIA-TYPES],
   * otherwise dct:format MAY be used with different values.</p>
   */
  @XmlElement(namespace = DCAT_URI)
  String mediaType;

  // TODO
  @XmlElement(namespace = DCAT_URI)
  List<String> packageFormat = new ArrayList<>();

  /**
   * The file format of the distribution.
   */
  @XmlElement(namespace = DCAT_URI)
  List<RdfResource> format = new ArrayList<>();


  @XmlElement(namespace = DCAT_URI)
  List<BigDecimal> spatialResolutionInMeters = new ArrayList<>();

  // TODO: Adapter
  @XmlElement(namespace = DCAT_URI)
  List<Duration> temporalResolution = new ArrayList<>();


  /**
   * A rights statement that concerns how the distribution is accessed.
   */
  @XmlElement(namespace = DCAT_URI)
  List<DcatAccessRights> accessRights = new ArrayList<>();


  /**
   * An established standard to which the distribution conforms.
   *
   * <p>This property SHOULD be used to indicate the model, schema, ontology, view or profile that
   * this representation of a dataset conforms to. This is (generally) a complementary concern to
   * the media-type or format.</p>
   */
  @XmlElement(namespace = DCT_URI)
  RdfResource conformsTo;

  /**
   * Date of formal issuance (e.g., publication) of the distribution.
   */
  @XmlElement(namespace = DCT_URI)
  Date issued;

  /**
   * Most recent date on which the distribution was changed, updated or modified.
   */
  @XmlElement(namespace = DCT_URI)
  Date modified;

  @XmlElement(namespace = DCT_URI)
  List<RdfResource> language = new ArrayList<>();

  /**
   * Information about rights held in and over the distribution.
   */
  @XmlElement(namespace = DCAT_URI)
  List<DcatAccessRights> rights = new ArrayList<>();

  @XmlElement(namespace = RDFS_URI)
  List<String> comment = new ArrayList<>();

  // TODO
  //  @XmlElement(namespace = DQV_URI)
  //  QualityMeasurement hasQualityMeasurement = new ArrayList<>();

  @XmlElement(namespace = FOAF_URI)
  List<DcatDocument> page;
}
