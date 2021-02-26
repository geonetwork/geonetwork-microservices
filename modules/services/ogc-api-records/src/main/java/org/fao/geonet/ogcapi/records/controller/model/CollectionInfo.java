package org.fao.geonet.ogcapi.records.controller.model;

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

/**
 * CollectionInfo entity.
 */
@JacksonXmlRootElement(localName = "CollectionInfo")
@XmlRootElement(name = "CollectionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class CollectionInfo   {
  @JsonProperty("id")
  @JacksonXmlProperty(localName = "id")
  private String id;

  @JsonProperty("title")
  @JacksonXmlProperty(localName = "title")
  private String title;

  @JsonProperty("description")
  @JacksonXmlProperty(localName = "description")
  private String description;

  @JsonProperty("links")
  @JacksonXmlProperty(localName = "links")
  
  private List<Link> links = new ArrayList<>();

  @JsonProperty("extent")
  @JacksonXmlProperty(localName = "extent")
  private Extent extent;

  @JsonProperty("crs")
  @JacksonXmlProperty(localName = "crs")
  
  private List<String> crs = null;

  public CollectionInfo id(String id) {
    this.id = id;
    return this;
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

  public CollectionInfo links(List<Link> links) {
    this.links = links;
    return this;
  }

  public CollectionInfo addLinksItem(Link linksItem) {
    this.links.add(linksItem);
    return this;
  }

  /**
   * Get links.
   */
  @ApiModelProperty(example = "[{\"href\":\"http://data.example.org/collections/buildings/items\",\"rel\":\"item\",\"type\":\"application/geo+json\",\"title\":\"Buildings\"},{\"href\":\"http://example.org/concepts/building.html\",\"rel\":\"describedBy\",\"type\":\"text/html\",\"title\":\"Coverage for buildings\"}]", required = true, value = "")
  public List<Link> getLinks() {
    return links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }

  public CollectionInfo extent(Extent extent) {
    this.extent = extent;
    return this;
  }

  /**
   * Get extent.
   */
  @ApiModelProperty(value = "")
  public Extent getExtent() {
    return extent;
  }

  public void setExtent(Extent extent) {
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
   * reference system that is used by default. This is always
   * \"http://www.opengis.net/def/crs/OGC/1.3/CRS84\", i.e. WGS84 longitude/latitude.
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
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  protected String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

