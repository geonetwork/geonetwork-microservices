/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.fao.geonet.ogcapi.records.model.OgcApiContact;
import org.fao.geonet.ogcapi.records.model.OgcApiExtent;
import org.fao.geonet.ogcapi.records.model.OgcApiLanguage;
import org.fao.geonet.ogcapi.records.model.OgcApiLink;
import org.fao.geonet.ogcapi.records.model.OgcApiSchema;
import org.fao.geonet.ogcapi.records.model.OgcApiTheme;

/**
 * cf. https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/catalog.yaml
 *
 * <p>CollectionInfo entity.
 */
@JacksonXmlRootElement(localName = "CollectionInfo")
@XmlRootElement(name = "CollectionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class CollectionInfo {

  /**
   * Fixed value of "Catalog".
   */
  //required cf. https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/catalog.yaml
  @JsonProperty("type")
  @JacksonXmlProperty(localName = "type")
  private String type = "catalog";

  // cf. https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/catalog.yaml
  // NOTE: spec says this should be "record" or ["record","catalog"].
  /**Fixed value of "record", "catalog" or both.*/
  @JsonProperty("itemType")
  @JacksonXmlProperty(localName = "itemType")
  private String itemType = "record";


  /**
   * The identifier of the catalog.
   */
  @JsonProperty("id")
  @JacksonXmlProperty(localName = "id")
  private String id;

  /**
   * A human-readable name given to the resource.
   */
  @JsonProperty("title")
  @JacksonXmlProperty(localName = "title")
  private String title;

  /**
   * A free-text account of the resource.
   */
  @JsonProperty("description")
  @JacksonXmlProperty(localName = "description")
  private String description;

  /**
   * links for this object.
   */
  @JsonProperty("links")
  @JacksonXmlProperty(localName = "links")
  @JsonInclude(Include.NON_EMPTY)
  private List<OgcApiLink> links = new ArrayList<>();

  /**
   * The spatiotemporal coverage of this catalog.
   */
  @JsonProperty("extent")
  @JacksonXmlProperty(localName = "extent")
  private OgcApiExtent extent;

  /**
   * The list of supported coordinate reference systems.
   */
  @JsonProperty("crs")
  @JacksonXmlProperty(localName = "crs")
  @JsonInclude(Include.NON_EMPTY)
  private List<String> crs = null;

  /**
   * A list of contacts qualified by their role(s) in association to the record or the resource
   * described by the record.
   */
  @JsonProperty("contacts")
  @JacksonXmlProperty(localName = "contacts")
  @JsonInclude(Include.NON_EMPTY)
  private List<OgcApiContact> contacts = null;

  /**
   * The date this record was created in the server. format: date-time
   */
  @JsonProperty("created")
  @JacksonXmlProperty(localName = "created")
  @JsonInclude(Include.NON_EMPTY)
  private String created = null;

  /**
   * The most recent date on which the record was changed. format: date-time
   */
  @JsonProperty("updated")
  @JacksonXmlProperty(localName = "updated")
  @JsonInclude(Include.NON_EMPTY)
  private String updated = null;

  /**
   * The topic or topics of the resource. Typically represented using free-form keywords, tags, key
   * phrases, or classification codes.
   */
  @JsonProperty("keywords")
  @JacksonXmlProperty(localName = "keywords")
  @JsonInclude(Include.NON_EMPTY)
  private List<String> keywords = null;

  /**
   * The language used for textual values in this record representation.
   */
  @JsonProperty("language")
  @JacksonXmlProperty(localName = "language")
  @JsonInclude(Include.NON_EMPTY)
  private OgcApiLanguage language = null;

  /**
   * This list of languages in which this record is available.
   */
  @JsonProperty("languages")
  @JacksonXmlProperty(localName = "languages")
  @JsonInclude(Include.NON_EMPTY)
  private List<OgcApiLanguage> languages = null;

  /**
   * A knowledge organization system used to classify the resource.
   **/
  @JsonProperty("themes")
  @JsonInclude(Include.NON_EMPTY)
  @JacksonXmlProperty(localName = "themes")
  private List<OgcApiTheme> themes = null;


  /**
   * A legal document under which the resource is made available. If the resource is being made
   * available under a common license then use an SPDX license id (https://spdx.org/licenses/). If
   * the resource is being made available under multiple common licenses then use an SPDX license
   * expression v2.3 string (https://spdx.github.io/spdx-spec/v2.3/SPDX-license-expressions/) If the
   * resource is being made available under one or more licenses that haven't been assigned an SPDX
   * identifier or one or more custom licenses then use a string value of 'other' and include one or
   * more links (rel="license") in the `link` section of the record to the file(s) that contains the
   * text of the license(s). There is also the case of a resource that is private or unpublished and
   * is thus unlicensed; in this case do not register such a resource in the catalog in the first
   * place since there is no point in making such a resource discoverable.
   */
  @JsonProperty("license")
  @JacksonXmlProperty(localName = "license")
  @JsonInclude(Include.NON_EMPTY)
  private String license = null;


  /**
   * A statement that concerns all rights not addressed by the license such as a copyright
   * statement.
   */
  @JsonProperty("rights")
  @JacksonXmlProperty(localName = "rights")
  @JsonInclude(Include.NON_EMPTY)
  private String rights = null;


  /**
   * Each string in the array SHALL be the name of a sortable.
   */
  @JsonProperty("defaultSortOrder")
  @JacksonXmlProperty(localName = "defaultSortOrder")
  @JsonInclude(Include.NON_EMPTY)
  private List<String> defaultSortOrder = null;


  //--------------------------------------------------------------------------
  // these are properties in the written spec, but not in the .yaml definition
  //--------------------------------------------------------------------------

  /**
   * The extensions/conformance classes used in this catalog object.
   */
  @JsonProperty("conformsTo")
  @JacksonXmlProperty(localName = "conformsTo")
  @JsonInclude(Include.NON_EMPTY)
  private String conformsTo = null;

  /**The list of languages in which records from the collection can be represented.*/
  @JsonProperty("recordLanguages")
  @JacksonXmlProperty(localName = "recordLanguages")
  @JsonInclude(Include.NON_EMPTY)
  private List<String> recordLanguages = null;


  /**
   * The name of the array property in the catalog used to encode records in-line.
   * The default value is records.
   */
  @JsonProperty("recordsArrayName")
  @JacksonXmlProperty(localName = "recordsArrayName")
  @JsonInclude(Include.NON_EMPTY)
  private String recordsArrayName = null;

  /**A list of schemes related to this catalog.*/
  @JsonProperty("schemes")
  @JacksonXmlProperty(localName = "schemes")
  @JsonInclude(Include.NON_EMPTY)
  private List<OgcApiSchema> schemes = null;


  //--------------------------------------------------------------------------


  public String getConformsTo() {
    return conformsTo;
  }

  public void setConformsTo(String conformsTo) {
    this.conformsTo = conformsTo;
  }

  public List<String> getRecordLanguages() {
    return recordLanguages;
  }

  public void setRecordLanguages(List<String> recordLanguages) {
    this.recordLanguages = recordLanguages;
  }

  public String getRecordsArrayName() {
    return recordsArrayName;
  }

  public void setRecordsArrayName(String recordsArrayName) {
    this.recordsArrayName = recordsArrayName;
  }

  public List<OgcApiSchema> getSchemes() {
    return schemes;
  }

  public void setSchemes(List<OgcApiSchema> schemes) {
    this.schemes = schemes;
  }

  public List<String> getDefaultSortOrder() {
    return defaultSortOrder;
  }

  public void setDefaultSortOrder(List<String> defaultSortOrder) {
    this.defaultSortOrder = defaultSortOrder;
  }

  public String getRights() {
    return rights;
  }

  public void setRights(String rights) {
    this.rights = rights;
  }

  public String getLicense() {
    return license;
  }

  public void setLicense(String license) {
    this.license = license;
  }

  public CollectionInfo id(String id) {
    this.id = id;
    return this;
  }

  public List<OgcApiTheme> getThemes() {
    return themes;
  }

  public void setThemes(List<OgcApiTheme> themes) {
    this.themes = themes;
  }

  public OgcApiLanguage getLanguage() {
    return language;
  }

  public void setLanguage(OgcApiLanguage language) {
    this.language = language;
  }

  public List<OgcApiLanguage> getLanguages() {
    return languages;
  }

  public void setLanguages(List<OgcApiLanguage> languages) {
    this.languages = languages;
  }

  public List<String> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<String> keywords) {
    this.keywords = keywords;
  }

  public String getCreated() {
    return created;
  }

  public void setCreated(String created) {
    this.created = created;
  }

  public String getUpdated() {
    return updated;
  }

  public void setUpdated(String updated) {
    this.updated = updated;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getItemType() {
    return itemType;
  }

  public void setItemType(String itemType) {
    this.itemType = itemType;
  }

  public List<OgcApiContact> getContacts() {
    return contacts;
  }

  public void setContacts(List<OgcApiContact> contacts) {
    this.contacts = contacts;
  }

  /**
   * Identifier of the collection used, for example, in URIs.
   */
  @ApiModelProperty(example = "buildings", required = true,
      value = "identifier of the collection used, for example, in URIs")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public CollectionInfo title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Human readable title of the collection.
   */
  @ApiModelProperty(example = "Buildings", value = "human readable title of the collection")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public CollectionInfo description(String description) {
    this.description = description;
    return this;
  }

  /**
   * A description of the data in the collection.
   */
  @ApiModelProperty(example = "Buildings in the city of Bonn.",
      value = "a description of the data in the collection")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public CollectionInfo links(List<OgcApiLink> links) {
    this.links = links;
    return this;
  }

  public CollectionInfo addLinksItem(OgcApiLink linksItem) {
    this.links.add(linksItem);
    return this;
  }

  /**
   * Get links.
   */
  @ApiModelProperty(example = "[{\"href\":\"http://data.example.org/collections/buildings/items\",\"rel\":\"item\",\"type\":\"application/geo+json\",\"title\":\"Buildings\"},{\"href\":\"http://example.org/concepts/building.html\",\"rel\":\"describedBy\",\"type\":\"text/html\",\"title\":\"Coverage for buildings\"}]", required = true, value = "")
  public List<OgcApiLink> getLinks() {
    return links;
  }

  public void setLinks(List<OgcApiLink> links) {
    this.links = links;
  }

  public CollectionInfo extent(OgcApiExtent extent) {
    this.extent = extent;
    return this;
  }

  /**
   * Get extent.
   */
  @ApiModelProperty(value = "")
  public OgcApiExtent getExtent() {
    return extent;
  }

  public void setExtent(OgcApiExtent extent) {
    this.extent = extent;
  }

  public CollectionInfo crs(List<String> crs) {
    this.crs = crs;
    return this;
  }

  /**
   * Adds a CrsItem.
   */
  public CollectionInfo addCrsItem(String crsItem) {
    if (this.crs == null) {
      this.crs = new ArrayList<>();
    }
    this.crs.add(crsItem);
    return this;
  }

  /**
   * The coordinate reference systems in which geometries may be retrieved. Coordinate reference
   * systems are identified by a URI. The first coordinate reference system is the coordinate
   * reference system that is used by default. This is always \"http://www.opengis.net/def/crs/OGC/1.3/CRS84\",
   * i.e. WGS84 longitude/latitude.
   */
  @ApiModelProperty(value = "The coordinate reference systems in which geometries may be retrieved. Coordinate reference systems are identified by a URI. The first coordinate reference system is the coordinate reference system that is used by default. This is always \"http://www.opengis.net/def/crs/OGC/1.3/CRS84\", i.e. WGS84 longitude/latitude.")
  public List<String> getCrs() {
    return crs;
  }

  public void setCrs(List<String> crs) {
    this.crs = crs;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CollectionInfo collectionInfo = (CollectionInfo) o;
    return Objects.equals(this.id, collectionInfo.id)
        && Objects.equals(this.title, collectionInfo.title)
        && Objects.equals(this.description, collectionInfo.description)
        && Objects.equals(this.links, collectionInfo.links)
        && Objects.equals(this.extent, collectionInfo.extent)
        && Objects.equals(this.crs, collectionInfo.crs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, description, links, extent, crs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CollectionInfo {\n");

    sb.append("    name: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
    sb.append("    extent: ").append(toIndentedString(extent)).append("\n");
    sb.append("    crs: ").append(toIndentedString(crs)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first
   * line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

