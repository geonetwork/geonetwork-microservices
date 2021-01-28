/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.gn;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.fao.geonet.domain.AbstractMetadata;
import org.fao.geonet.domain.MetadataDraft;
import org.locationtech.jts.geom.Coordinate;

@Data
@EqualsAndHashCode(callSuper = true)
@XmlRootElement(name = "indexRecord")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexRecord extends IndexDocument {

  private Integer internalId;
  private String metadataIdentifier;

  private IndexDocumentType docType;

  // Unused for now. Was used to store XML document.
  private String document;

  // eg. iso19139
  private String documentStandard;
  private String schema;

  // eg. ISO 19119/2005
  private String standardName;

  private String draft;
  private Character isTemplate;
  private String root;

  private String indexingDate;

  @JsonIgnore
  private String dateStamp;
  private String changeDate;
  private String createDate;

  @JsonProperty(IndexRecordFieldNames.resourceDate)
  private List<ResourceDate> resourceDate = new ArrayList<>();

  private Integer owner;
  private Integer groupOwner;
  private String recordOwner;
  private String userinfo;
  private String sourceCatalogue;
  private String logo;

  @XmlElement(name = IndexRecordFieldNames.isPublishedToAll)
  private boolean publishedToAll;
  private List<String> groupPublished = new ArrayList<>();

  // TODO: op0: "1",

  private int popularity;
  private String mainLanguage;
  private List<String> resourceType = new ArrayList<>();

  private Integer valid;
  private Integer feedbackCount;
  private Integer rating;

  @XmlElement(name = IndexRecordFieldNames.isHarvested)
  private boolean harvested;
  private String harvesterUuid;

  private boolean hasxlinks;
  private boolean hasOverview;

  private boolean isMultilingual;
  private List<String> otherLanguage = new ArrayList<>();
  private List<String> otherLanguageId = new ArrayList<>();
  private List<String> resourceLanguage = new ArrayList<>();

  //  resourceTitleObject: {
  //    default: "Organisation de la protection civile dans
  //    le canton de Fribourg - Organisation des Zivilschutzes im Kanton Freiburg",
  //        langfre: "Organisation de la protection civile dans le canton de Fribourg
  //        - Organisation des Zivilschutzes im Kanton Freiburg"
  @JsonProperty(IndexRecordFieldNames.resourceTitle)
  Map<String, String> resourceTitle = new HashMap<>();

  @JsonProperty(IndexRecordFieldNames.resourceAltTitle)
  List<HashMap<String, String>> resourceAltTitle = new ArrayList<>();

  @JsonProperty(IndexRecordFieldNames.resourceAbstract)
  Map<String, String> resourceAbstract = new HashMap<>();

  @JsonProperty(IndexRecordFieldNames.resourceCredit)
  Map<String, String> resourceCredit = new HashMap<>();

  @JsonProperty(IndexRecordFieldNames.tag)
  ArrayList<HashMap<String, String>> tag = new ArrayList<>();


  @JsonProperty(IndexRecordFieldNames.resourceIdentifier)
  private ArrayList<String> resourceIdentifier;

  @JsonProperty(IndexRecordFieldNames.org)
  private List<String> org;
  @JsonProperty(IndexRecordFieldNames.orgForResource)
  private List<String> orgForResource;
  // others eg. pointOfContactOrgForResource are in other properties
  private List<Contact> contact = new ArrayList<>();
  private List<Contact> contactForResource = new ArrayList<>();


  @JsonProperty(IndexRecordFieldNames.format)
  private List<String> formats = new ArrayList<>();
  @JsonProperty(IndexRecordFieldNames.coordinateSystem)
  private List<String> coordinateSystem = new ArrayList<>();

  @JsonProperty(IndexRecordFieldNames.serviceType)
  private List<String> serviceTypes = new ArrayList<>();

  private List<Overview> overview = new ArrayList<>();

  @JsonProperty(IndexRecordFieldNames.recordLink)
  private List<RecordLink> associatedRecords;

  @JsonProperty(IndexRecordFieldNames.link)
  private List<Link> links = new ArrayList<>();


  //  resourceTemporalDateRange: [{
  //    gte: "2017-02-03T14:00:00",
  //    lte: "2017-02-03T14:00:00"
  private List<DateRange> resourceTemporalDateRange = new ArrayList<>();
  private List<DateRange> resourceTemporalExtentDateRange = new ArrayList<>();

  // TODO XML
  @XmlTransient
  @JsonProperty(IndexRecordFieldNames.location)
  @JsonDeserialize(using = LocationDeserializer.class)
  @JsonSerialize(using = LocationSerializer.class)
  private List<Coordinate> locations;


  // TODO XML
  @XmlTransient
  @JsonProperty(IndexRecordFieldNames.geom)
  @JsonDeserialize(using = NodeTreeAsStringDeserializer.class)
  private List<String> geometries;

  //  @JsonAnyGetter
  private Map<String, ArrayList<String>> otherProperties = new HashMap<>();

  /**
   * Collect all other properties in a map.
   */
  @JsonAnySetter
  public void ignored(String name, Object value) {
    // Ignore class fields.
    try {
      IndexRecord.class.getDeclaredField(name);
    } catch (NoSuchFieldException e) {
      ArrayList<String> s = otherProperties.get(name);
      if (s == null) {
        s = new ArrayList<>(1);
        s.add(value.toString());
        otherProperties.put(name, s);
      } else {
        s.add(value.toString());
      }
    }
  }


  /**
   * Record to be loaded in the index.
   */
  public IndexRecord(AbstractMetadata r) {
    boolean isDraft = r instanceof MetadataDraft;

    this.setId(r.getUuid() + (isDraft ? "-draft" : ""));

    // Based on https://github.com/geonetwork/core-geonetwork/blob/4.0.x/core/src/main/java/org/fao/geonet/kernel/datamanager/base/BaseMetadataIndexer.java#L325
    // XML document as string to be further processed in XSLT
    this.setDocument(r.getData());

    this.setInternalId(r.getId());
    this.setMetadataIdentifier(r.getUuid());
    this.setSchema(r.getDataInfo().getSchemaId());
    this.setDocType(IndexDocumentType.metadata);
    this.setDraft(isDraft ? "y" : "n");
    this.setIsTemplate(r.getDataInfo().getType().code);

    this.setCreateDate(r.getDataInfo().getCreateDate().getDateAndTime());
    this.setChangeDate(r.getDataInfo().getChangeDate().getDateAndTime());

    this.setOwner(r.getSourceInfo().getOwner());
    this.setGroupOwner(r.getSourceInfo().getGroupOwner());
    // this.setRecordOwner("admin admin");
    //userinfo: "admin|admin|admin|Administrator",
    this.setSourceCatalogue(r.getSourceInfo().getSourceId());
    this.setHarvested(r.getHarvestInfo().isHarvested());
    this.setHarvesterUuid(r.getHarvestInfo().getUuid());
    this.setLogo("");
    this.setPublishedToAll(true);

    this.setPopularity(r.getDataInfo().getPopularity());
    this.setRating(r.getDataInfo().getRating());
  }

  public IndexRecord() {
  }
}
