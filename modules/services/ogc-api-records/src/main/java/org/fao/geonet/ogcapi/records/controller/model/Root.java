package org.fao.geonet.ogcapi.records.rest.ogc.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.fao.geonet.ogcapi.records.rest.ogc.model.Link;
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.xml.bind.annotation.*;

/**
 * Root
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-02-18T08:34:42.274802+01:00[Europe/Madrid]")
@JacksonXmlRootElement(localName = "Root")
@XmlRootElement(name = "Root")
@XmlAccessorType(XmlAccessType.FIELD)
public class Root   {
  @JsonProperty("links")
  @JacksonXmlProperty(localName = "links")
  
  private List<Link> links = new ArrayList<>();

  public Root links(List<Link> links) {
    this.links = links;
    return this;
  }

  public Root addLinksItem(Link linksItem) {
    this.links.add(linksItem);
    return this;
  }

  /**
   * Get links
   * @return links
  */
  @ApiModelProperty(example = "[{\"href\":\"http://data.example.org/\",\"rel\":\"self\",\"type\":\"application/json\",\"title\":\"this document\"},{\"href\":\"http://data.example.org/api\",\"rel\":\"service\",\"type\":\"application/openapi+json;version=3.0\",\"title\":\"the API definition\"},{\"href\":\"http://data.example.org/conformance\",\"rel\":\"conformance\",\"type\":\"application/json\",\"title\":\"OGC conformance classes implemented by this API\"},{\"href\":\"http://data.example.org/collections\",\"title\":\"Metadata about the resource collections\"}]", required = true, value = "")
  public List<Link> getLinks() {
    return links;
  }

  public void setLinks(List<Link> links) {
    this.links = links;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Root root = (Root) o;
    return Objects.equals(this.links, root.links);
  }

  @Override
  public int hashCode() {
    return Objects.hash(links);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Root {\n");
    
    sb.append("    links: ").append(toIndentedString(links)).append("\n");
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

