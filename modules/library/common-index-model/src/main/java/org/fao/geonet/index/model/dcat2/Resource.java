/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.dcat2;

import static org.fao.geonet.index.model.dcat2.Namespaces.DCAT_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.DCT_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.PROV_URI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@XmlRootElement(namespace = DCAT_URI)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

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
   * An established standard to which the described resource conforms.
   *
   * <p>This property SHOULD be used to indicate the model, schema, ontology, view or profile that
   * the catalog record metadata conforms to.</p>
   */
  @XmlElement(namespace = DCT_URI)
  RdfResource conformsTo;


  @XmlElement(namespace = DCT_URI)
  Date created;

  /**
   * The date of listing (i.e. formal recording) of the corresponding dataset or service in the
   * catalog.
   *
   * <p>This indicates the date of listing the dataset in the catalog and not the publication date
   * of the dataset itself.</p>
   */
  @XmlElement(namespace = DCT_URI)
  Date issued;

  /**
   * Most recent date on which the catalog entry was changed, updated or modified.
   *
   * <p>This indicates the date of last change of a catalog entry, i.e. the catalog metadata
   * description of the dataset, and not the date of the dataset itself.</p>
   */
  @XmlElement(namespace = DCT_URI)
  Date modified;

  // Can be a LinguisticSystem
  @XmlElement(namespace = DCT_URI)
  List<RdfResource> language = new ArrayList<>();

  @XmlElement(namespace = PROV_URI)
  List<DcatQualifiedAttribution> qualifiedAttribution = new ArrayList();


  @XmlElement(namespace = DCAT_URI)
  List<DcatContactPoint> contactPoint = new ArrayList();

  @XmlElement(namespace = DCT_URI)
  List<FoafOrganization> creator = new ArrayList();

  /**
   * A type of organisation that acts as a publisher.
   */
  @XmlElement(namespace = DCT_URI)
  List<FoafOrganization> publisher = new ArrayList();

  /**
   * An association class for attaching additional information to a relationship between DCAT
   * Resources.
   */
  @XmlElement(namespace = DCAT_URI)
  List<DcatRelationship> qualifiedRelation = new ArrayList();
}
