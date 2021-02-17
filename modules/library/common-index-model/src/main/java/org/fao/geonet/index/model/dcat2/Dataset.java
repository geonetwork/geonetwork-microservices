package org.fao.geonet.index.model.dcat2;

import static org.fao.geonet.index.model.dcat2.Namespaces.ADMS_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.DCAT_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.DCT_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.FOAF_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.OWL_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.PROV_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.RDFS_URI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * A conceptual entity that represents the information published.
 *
 * <p>GeoDCAT-AP 2 model.
 * <p>
 * https://www.w3.org/TR/vocab-dcat-2/ https://semiceu.github.io/GeoDCAT-AP/drafts/latest/
 * https://semiceu.github.io/GeoDCAT-AP/drafts/latest/geodcat-ap_v2.0.0.svg
 */
@XmlRootElement(namespace = DCAT_URI)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dataset {

  @NonNull
  @XmlElement(namespace = DCT_URI)
  List<String> title = new ArrayList();

  @NonNull
  @XmlElement(namespace = DCT_URI)
  List<String> description = new ArrayList();

  /**
   * A unique identifier of the item.
   */
  @XmlElement(namespace = DCT_URI)
  List<String> identifier = new ArrayList();

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
   * <p>
   * In GeoDCAT-AP, this property SHOULD take as value one of the URIs of the "Resource types" code
   * list operated by the INSPIRE Registry [INSPIRE-RT]. For Datasets, the possible values are those
   * corresponding to "Spatial data set" and "Spatial data set series".
   */
  // Can also be a RdfResource
  @XmlElement(namespace = DCT_URI)
  Subject type;

  //  /**
  //   * An identifier in a particular context, consisting of the string that is the identifier; an
  //   * optional identifier for the identifier scheme;
  //   an optional identifier for the version of the
  //   * identifier scheme; an optional identifier for the agency that manages the identifier scheme
  //   */
  //  @XmlElement(name = "identifier", namespace = ADMS_URI)
  //  List<AdmsIdentifier> admsIdentifier = new ArrayList();

  /**
   * This property contains a description of the differences between this version and a previous
   * version of the Dataset.
   */
  @XmlElement(namespace = ADMS_URI)
  List<String> versionNotes = new ArrayList();

  @XmlElement(namespace = DCAT_URI)
  List<DcatDocument> landingPage = new ArrayList();

  @XmlElement(namespace = FOAF_URI)
  List<DcatDocument> page;

  @XmlElement(namespace = DCAT_URI)
  List<DcatDistributionContainer> distribution = new ArrayList();


  /**
   * An association class for attaching additional information to a relationship between DCAT Resources.
   */
  @XmlElement(namespace = DCAT_URI)
  List<DcatRelationship> qualifiedRelation = new ArrayList();

  @XmlElement(namespace = DCT_URI)
  List<RdfResource> isReferencedBy = new ArrayList<>();

  @XmlElement(namespace = DCT_URI)
  List<RdfResource> relation = new ArrayList<>();

  @XmlElement(namespace = DCT_URI)
  List<RdfResource> isVersionOf = new ArrayList<>();

  @XmlElement(namespace = DCT_URI)
  List<RdfResource> hasVersionOf = new ArrayList<>();

  @XmlElement(namespace = DCT_URI)
  List<RdfResource> source = new ArrayList<>();

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
   * This property refers to the frequency at which the Dataset is updated.
   *
   * <p>eg. http://publications.europa.eu/resource/authority/frequency/IRREG</p>
   */
  @XmlElement(namespace = DCT_URI)
  RdfResource accrualPeriodicity;

  public static String EU_PUBLICATION_URI_PREFIX = "http://publications.europa.eu/resource/authority/";
  public static String ACCRUAL_PERIODICITY_URI_PREFIX = EU_PUBLICATION_URI_PREFIX + "frequency/";
  public static Map<String, String> ACCRUAL_PERIODICITY_TO_ISO = Map.ofEntries(
      new AbstractMap.SimpleEntry<String, String>("continual", "CONT"),
      new AbstractMap.SimpleEntry<String, String>("daily", "DAILY"),
      new AbstractMap.SimpleEntry<String, String>("weekly", "WEEKLY"),
      new AbstractMap.SimpleEntry<String, String>("fortnightly", "BIWEEKLY"),
      new AbstractMap.SimpleEntry<String, String>("monthly", "MONTHLY"),
      new AbstractMap.SimpleEntry<String, String>("quarterly", "QUARTERLY"),
      new AbstractMap.SimpleEntry<String, String>("biannually", "ANNUAL_2"),
      new AbstractMap.SimpleEntry<String, String>("annually", "ANNUAL"),
      new AbstractMap.SimpleEntry<String, String>("asNeeded", "asNeeded"),
      new AbstractMap.SimpleEntry<String, String>("irregular", "IRREG"),
      new AbstractMap.SimpleEntry<String, String>("unknown", "UNKNOWN")
  );


  @XmlElement(namespace = DCT_URI)
  RdfResource conformsTo;

  @XmlElement(namespace = DCT_URI)
  Date created;

  @XmlElement(namespace = DCT_URI)
  Date issued;

  @XmlElement(namespace = DCT_URI)
  Date modified;

  /**
   * A statement of any changes in ownership and custody of a resource since its creation that are
   * significant for its authenticity, integrity, and interpretation
   */
  @XmlElement(namespace = DCT_URI)
  String provenance;

  // Can be a LinguisticSystem
  @XmlElement(namespace = DCT_URI)
  List<RdfResource> language = new ArrayList<>();

  /**
   * Represents the evaluation of a given resource (as a Data Service, Dataset, or Distribution) against a specific quality metric.
   *
   * In GeoDCAT-AP, this class corresponds to the notion of "spatial resolution", as defined in [INSPIRE-MD-REG], [ISO-19115], and [ISO-19115-1].
   */
  // TODO
  //  @XmlElement(namespace = DQV_URI)
  //  QualityMeasurement hasQualityMeasurement = new ArrayList();

  /**
   * This property contains a version number or other version designation of the Dataset.
   */
  @XmlElement(namespace = OWL_URI)
  String versionInfo;

  @XmlElement(namespace = PROV_URI)
  List<DcatQualifiedAttribution> qualifiedAttribution = new ArrayList();

  @XmlElement(namespace = PROV_URI)
  List<DcatActivity> wasGeneratedBy = new ArrayList();

  @XmlElement(namespace = PROV_URI)
  List<DcatActivity> wasUsedBy = new ArrayList();

  @XmlElement(namespace = DCAT_URI)
  List<DcatContactPoint> contactPoint = new ArrayList();

  @XmlElement(namespace = DCT_URI)
  List<FoafAgent> rightsHolder = new ArrayList();
  @XmlElement(namespace = DCT_URI)
  List<FoafAgent> creator = new ArrayList();
  /**
   * A type of organisation that acts as a publisher.
   */
  @XmlElement(namespace = DCT_URI)
  List<FoafAgent> publisher = new ArrayList();

  @XmlElement(namespace = RDFS_URI)
  List<String> comment = new ArrayList();
}
