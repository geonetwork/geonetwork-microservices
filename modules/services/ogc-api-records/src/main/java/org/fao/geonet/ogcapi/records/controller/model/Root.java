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
import org.fao.geonet.ogcapi.records.model.OgcApiLink;

/**
 * Root entity.
 *
 * <p>Used for the landing page.
 */
@JacksonXmlRootElement(localName = "Root")
@XmlRootElement(name = "Root")
@XmlAccessorType(XmlAccessType.FIELD)
public class Root {

  /**
   * This is the collection info for the main-portal.
   *
   * <p>THIS IS NON-STANDARD (not in the ogcapi spec)!
   *
   * <p>The landing page JSON also includes a new property systemInfo which contains a catalog.yaml
   * object in it.
   * I'm not sure if this is allowed in the spec, but it allows for more metadata about the
   * entire GN system ("ogcapi-records" server). This is useful for making a nicer landing page
   * (cf. pygeoapi's landing page).
   */
  @JsonProperty("systemInfo")
  @JacksonXmlProperty(localName = "systemInfo")
  @JsonInclude(Include.NON_EMPTY)
  private CollectionInfo systemInfo;


  @JsonProperty("title")
  @JacksonXmlProperty(localName = "title")
  private String title;
  @JsonProperty("description")
  @JacksonXmlProperty(localName = "description")
  private String description;
  @JsonProperty("links")
  @JacksonXmlProperty(localName = "links")
  private List<OgcApiLink> links = new ArrayList<>();

  /**
   * Get title.
   */
  @ApiModelProperty(example = "The OpenAPI documentation", value = "")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Root title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Get description.
   */
  @ApiModelProperty(example = "The OpenAPI documentation", value = "")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Root description(String description) {
    this.description = description;
    return this;
  }

  public Root links(List<OgcApiLink> links) {
    this.links = links;
    return this;
  }

  public Root addLinksItem(OgcApiLink linksItem) {
    this.links.add(linksItem);
    return this;
  }

  /**
   * Get links.
   */
  @ApiModelProperty(example = "[{\"href\":\"http://data.example.org/\",\"rel\":\"self\",\"type\":\"application/json\",\"title\":\"this document\"},{\"href\":\"http://data.example.org/api\",\"rel\":\"service\",\"type\":\"application/openapi+json;version=3.0\",\"title\":\"the API definition\"},{\"href\":\"http://data.example.org/conformance\",\"rel\":\"conformance\",\"type\":\"application/json\",\"title\":\"OGC conformance classes implemented by this API\"},{\"href\":\"http://data.example.org/collections\",\"title\":\"Metadata about the resource collections\"}]", required = true, value = "")
  public List<OgcApiLink> getLinks() {
    return links;
  }

  public void setLinks(List<OgcApiLink> links) {
    this.links = links;
  }

  public CollectionInfo getSystemInfo() {
    return systemInfo;
  }

  public void setSystemInfo(CollectionInfo systemInfo) {
    this.systemInfo = systemInfo;
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

