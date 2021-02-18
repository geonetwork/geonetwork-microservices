package org.fao.geonet.ogcapi.records.rest.ogc.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.fao.geonet.ogcapi.records.rest.ogc.model.Extent;
import org.fao.geonet.ogcapi.records.rest.ogc.model.Link;
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.xml.bind.annotation.*;

/**
 * CollectionInfo
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-02-18T08:34:42.274802+01:00[Europe/Madrid]")
@JacksonXmlRootElement(localName = "CollectionInfo")
@XmlRootElement(name = "CollectionInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class CollectionInfo   {
  @JsonProperty("name")
  @JacksonXmlProperty(localName = "name")
  private String name;

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

  public CollectionInfo name(String name) {
    this.name = name;
    return this;
  }

  /**
   * identifier of the collection used, for example, in URIs
   * @return name
  */
  @ApiModelProperty(example = "buildings", required = true, value = "identifier of the collection used, for example, in URIs")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CollectionInfo title(String title) {
    this.title = title;
    return this;
  }

  /**
   * human readable title of the collection
   * @return title
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
   * a description of the data in the collection
   * @return description
  */
  @ApiModelProperty(example = "Buildings in the city of Bonn.", value = "a description of the data in the collection")
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
   * Get links
   * @return links
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
   * Get extent
   * @return extent
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

  public CollectionInfo addCrsItem(String crsItem) {
    if (this.crs == null) {
      this.crs = new ArrayList<>();
    }
    this.crs.add(crsItem);
    return this;
  }

  /**
   * The coordinate reference systems in which geometries may be retrieved. Coordinate reference systems are identified by a URI. The first coordinate reference system is the coordinate reference system that is used by default. This is always \"http://www.opengis.net/def/crs/OGC/1.3/CRS84\", i.e. WGS84 longitude/latitude.
   * @return crs
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
    return Objects.equals(this.name, collectionInfo.name) &&
        Objects.equals(this.title, collectionInfo.title) &&
        Objects.equals(this.description, collectionInfo.description) &&
        Objects.equals(this.links, collectionInfo.links) &&
        Objects.equals(this.extent, collectionInfo.extent) &&
        Objects.equals(this.crs, collectionInfo.crs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, title, description, links, extent, crs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CollectionInfo {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

