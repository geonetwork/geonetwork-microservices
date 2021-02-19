/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.dcat2;

import static org.fao.geonet.index.model.dcat2.Namespaces.DCAT_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.DCT_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.PROV_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.RDFS_URI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Resource extends Base {

  // "Replaced by dcat:theme."
  @Deprecated
  @XmlElement(namespace = DCT_URI)
  List<Subject> subject = new ArrayList();

  @XmlElement(namespace = DCAT_URI)
  List<Subject> keyword = new ArrayList();

  @XmlElement(namespace = DCAT_URI)
  List<Subject> theme = new ArrayList();

  /**
   * This property refers to the type of the Dataset. A controlled vocabulary for the values has not
   * been established in [DCAT-AP].
   *
   * <p>In GeoDCAT-AP, this property SHOULD take as value one of the URIs of the "Resource types"
   * code list operated by the INSPIRE Registry [INSPIRE-RT]. For Datasets, the possible values are
   * those corresponding to "Spatial data set" and "Spatial data set series".
   */
  @XmlElements({
      @XmlElement(namespace = DCT_URI, type = Subject.class),
      @XmlElement(namespace = DCT_URI, type = RdfResource.class)
  })
  List<Object> type;

  @XmlElement(namespace = DCAT_URI)
  List<DcatDocument> landingPage = new ArrayList();

  @XmlElement(namespace = DCT_URI)
  List<RdfResource> isReferencedBy = new ArrayList<>();

  @XmlElement(namespace = DCT_URI)
  List<RdfResource> relation = new ArrayList<>();

  /**
   * This property MAY include information regarding access or restrictions based on privacy,
   * security, or other policies.
   *
   * <p>
   * For INSPIRE metadata, this property SHOULD be used with the URIs of the "Limitations on public
   * access" code list operated by the INSPIRE Registry</p>
   */
  @XmlElement(namespace = DCAT_URI)
  List<DcatAccessRights> accessRights = new ArrayList();

  @XmlElement(namespace = DCT_URI)
  DcatLicenseDocumentContainer license;

  /**
   * An association class for attaching additional information to a relationship between DCAT
   * Resources.
   */
  @XmlElement(namespace = DCAT_URI)
  List<DcatRelationship> qualifiedRelation = new ArrayList();


  /**
   * Minimum spatial separation resolvable in a dataset, measured in meters.
   *
   * <p>If the dataset is an image or grid this should correspond to the spacing of items. For
   * other kinds of spatial datasets, this property will usually indicate the smallest distance
   * between items in the dataset.</p>
   */
  @XmlElement(namespace = DCAT_URI)
  List<BigDecimal> spatialResolutionInMeters = new ArrayList();

  /**
   * Minimum time period resolvable in the dataset.
   *
   * <p>
   * If the dataset is a time-series this should correspond to the spacing of items in the series.
   * For other kinds of dataset, this property will usually indicate the smallest time difference
   * between items in the dataset.</p>
   */
  // TODO: Adapter
  @XmlElement(namespace = DCAT_URI)
  List<Duration> temporalResolution = new ArrayList();


  /**
   * The geographical area covered by the dataset.
   */
  @XmlElement(namespace = DCT_URI)
  List<DctSpatial> spatial = new ArrayList<>();

  @XmlElement(namespace = DCT_URI)
  List<DctTemporal> temporal;

  //  /**
  //   * Represents the evaluation of a given resource
  //   * (as a Data Service, Dataset, or Distribution)
  //   * against a specific quality metric.
  //   *
  //   * In GeoDCAT-AP, this class corresponds to the notion of
  //   * "spatial resolution", as defined in [INSPIRE-MD-REG],
  //   * [ISO-19115], and [ISO-19115-1].
  //   */
  // TODO
  //  @XmlElement(namespace = DQV_URI)
  //  QualityMeasurement hasQualityMeasurement = new ArrayList();

  @XmlElement(namespace = PROV_URI)
  List<DcatActivity> wasUsedBy = new ArrayList();

  @XmlElement(namespace = RDFS_URI)
  List<String> comment = new ArrayList();
}
