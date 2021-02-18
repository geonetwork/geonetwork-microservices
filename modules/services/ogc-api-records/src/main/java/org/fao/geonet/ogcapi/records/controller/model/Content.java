package org.fao.geonet.ogcapi.records.rest.ogc.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.fao.geonet.ogcapi.records.rest.ogc.model.CollectionInfo;
import org.fao.geonet.ogcapi.records.rest.ogc.model.Link;
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.xml.bind.annotation.*;

/**
 * Content
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-02-18T08:34:42.274802+01:00[Europe/Madrid]")
@JacksonXmlRootElement(localName = "Content")
@XmlRootElement(name = "Content")
@XmlAccessorType(XmlAccessType.FIELD)
public class Content   {
  @JsonProperty("links")
  @JacksonXmlProperty(localName = "links")
  
  private List<Link> links = new ArrayList<>();

  @JsonProperty("collections")
  @JacksonXmlProperty(localName = "collections")
  
  private List<CollectionInfo> collections = new ArrayList<>();

  public Content links(List<Link> links) {
    this.links = links;
    return this;
  }

  public Content addLinksItem(Link linksItem) {
    this.links.add(linksItem);
    return this;
  }

  /**
   * Get links
   * @return links
  */
  @ApiModelProperty(example = "[{\"href\":\"http://data.example.org/collections.json\",\"rel\":\"self\",\"type\":\"application/json\",\"title\":\"this document\"},{\"href\":\"http://data.example.org/collections.html\",\"rel\":\"alternate\",\"type\":\"text/html\",\"title\":\"this document as HTML\"},{\"href\":\"http://schemas.example.org/1.0/foobar.xsd\",\"rel\":\"describedBy\",\"type\":\"application/xml\",\"title\":\"XML schema for Acme Corporation data\"}]", required = true, value = "")
  public List<Link> getLinks() {
    return links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }

  public Content collections(List<CollectionInfo> collections) {
    this.collections = collections;
    return this;
  }

  public Content addCollectionsItem(CollectionInfo collectionsItem) {
    this.collections.add(collectionsItem);
    return this;
  }

  /**
   * Get collections
   * @return collections
  */
  @ApiModelProperty(required = true, value = "")
  public List<CollectionInfo> getCollections() {
    return collections;
  }

  public void setCollections(List<CollectionInfo> collections) {
    this.collections = collections;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Content content = (Content) o;
    return Objects.equals(this.links, content.links) &&
        Objects.equals(this.collections, content.collections);
  }

  @Override
  public int hashCode() {
    return Objects.hash(links, collections);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Content {\n");
    
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
    sb.append("    collections: ").append(toIndentedString(collections)).append("\n");
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

