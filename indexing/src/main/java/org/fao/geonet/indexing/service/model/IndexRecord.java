/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.indexing.service.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import org.fao.geonet.domain.AbstractMetadata;
import org.fao.geonet.domain.MetadataDraft;

@Data
@XmlRootElement(name = "indexRecord")
@XmlAccessorType(XmlAccessType.FIELD)
public class IndexRecord extends IndexDocument {

  private Integer internalId;
  private String metadataIdentifier;

  private String docType;
  private String document;
  private String documentStandard;
  private String standardName;
  private String schema;
  private String draft;
  private String isTemplate;
  private String root;

  private String indexingDate;
  private String changeDate;
  private String createDate;

  private Integer owner;
  private Integer groupOwner;
  private String recordOwner;
  private String userinfo;
  private String sourceCatalogue;
  private String logo;

  @XmlElement(name = "isPublishedToAll")
  private boolean publishedToAll;
  private String groupPublished;

  // TODO: op0: "1",

  private int popularity;
  private String mainLanguage;
  private String resourceType;

  private Integer valid;
  private Integer feedbackCount;
  private Integer rating;

  @XmlElement(name = "isHarvested")
  private boolean harvested;
  private String harvesterUuid;
  private boolean hasxlinks;

  private boolean isMultilingual;
  private List<String> otherLanguage = new ArrayList<>();
  private List<String> otherLanguageId = new ArrayList<>();

  //  resourceTitleObject: {
  //    default: "Organisation de la protection civile dans
  //    le canton de Fribourg - Organisation des Zivilschutzes im Kanton Freiburg",
  //        langfre: "Organisation de la protection civile dans le canton de Fribourg
  //        - Organisation des Zivilschutzes im Kanton Freiburg"
  Map<String, String> resourceTitle = new HashMap<>();
  Map<String, String> resourceAbstract = new HashMap<>();

  // TODO: Object field
  //  contact: [
  //  {
  //    organisation: "Centre de Compétence SIT",
  //        role: "pointOfContact",
  //      email: "Vincent.Grandgirard@fr.ch",
  //      website: "https://www.fr.ch/scg/territoire-amenagement-
  //      et-constructions/cartes/centre-de-competence-sit",
  //      logo: "",
  //      individual: "",
  //      position: "",
  //      phone: "",
  //      address: "Fribourg, Service du cadastre et de la géomatique (SCG), 1701, CH"
  //  }
  //  private List<Link> resourceLinks;

  // TODO: codelist_characterSet
  // Translate/Multilingual ?

  // TODO: range
  //  resourceTemporalDateRange: [
  //  {
  //    gte: "2017-02-03T14:00:00",
  //        lte: "2017-02-03T14:00:00"

  // TODO: Shape
  // TODO: geom
  //  geom: {
  //    type: "Polygon",
  //        coordinates: [
  //[
  //[
  //    6.74271,
  //        46.439772
  //],
  //[
  //    7.381147,
  //        46.439772
  //],
  //[
  //    7.381147,
  //        47.008734
  //],
  //[
  //    6.74271,
  //        47.008734
  //],
  //[
  //    6.74271,
  //        46.439772
  //]
  //]
  //]

  //  @JsonAnyGetter
  private Map<String, String> otherProperties = new HashMap();

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
    this.setDocType("metadata");
    this.setDraft(isDraft ? "y" : "n");
    this.setIsTemplate(r.getDataInfo().getTitle());

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
