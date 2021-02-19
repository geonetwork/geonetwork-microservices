package org.fao.geonet.ogcapi.records.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Link entity.
 */
@JacksonXmlRootElement(localName = "Link")
@XmlRootElement(name = "Link")
@XmlAccessorType(XmlAccessType.FIELD)
public class Link   {
  @JsonProperty("href")
  @JacksonXmlProperty(localName = "href")
  private String href;

  @JsonProperty("rel")
  @JacksonXmlProperty(localName = "rel")
  private String rel;

  @JsonProperty("type")
  @JacksonXmlProperty(localName = "type")
  private String type;

  @JsonProperty("hreflang")
  @JacksonXmlProperty(localName = "hreflang")
  private String hreflang;

  public Link href(String href) {
    this.href = href;
    return this;
  }

  /**
   * Get href.
   * */
  @ApiModelProperty(required = true, value = "")
  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public Link rel(String rel) {
    this.rel = rel;
    return this;
  }

  /**
   * Get rel.
   */
  @ApiModelProperty(example = "prev", value = "")
  public String getRel() {
    return rel;
  }

  public void setRel(String rel) {
    this.rel = rel;
  }

  public Link type(String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type.
   */
  @ApiModelProperty(example = "application/geo+json", value = "")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Link hreflang(String hreflang) {
    this.hreflang = hreflang;
    return this;
  }

  /**
   * Get hreflang.
   */
  @ApiModelProperty(example = "en", value = "")
  public String getHreflang() {
    return hreflang;
  }

  public void setHreflang(String hreflang) {
    this.hreflang = hreflang;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Link link = (Link) o;
    return Objects.equals(this.href, link.href)
        && Objects.equals(this.rel, link.rel)
        && Objects.equals(this.type, link.type)
        && Objects.equals(this.hreflang, link.hreflang);
  }

  @Override
  public int hashCode() {
    return Objects.hash(href, rel, type, hreflang);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Link {\n");
    
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    rel: ").append(toIndentedString(rel)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    hreflang: ").append(toIndentedString(hreflang)).append("\n");
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

