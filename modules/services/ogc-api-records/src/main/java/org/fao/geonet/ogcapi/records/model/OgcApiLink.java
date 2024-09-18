/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/linkBase.yaml
 *
 * <p>also see: https://schemas.opengis.net/ogcapi/features/part1/1.0/openapi/schemas/link.yaml
 *
 * <p>NOTE:
 *     These specs aren't really consistent.  The first:
 *        a) doesn't have a href (this is obviously a mistake).
 *        b) has a created/updated field (not in the other spec).  Recommend not using this.
 *
 *  <p>Note - there is a length field, but its value isn't defined. Recommend not using this.
 *
 * <p>Represents a link.
 */
@XmlRootElement(name = "link")
@XmlAccessorType(XmlAccessType.FIELD)
public class OgcApiLink {

  /**
   * The type or semantics of the relation. example: alternate
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "rel")
  @XmlElement(name = "rel")
  private String rel;

  /**
   * A hint indicating what the media type of the result of dereferencing the link should be.
   * example: application/geo+json
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "type")
  @XmlElement(name = "type")
  private String type;

  /**
   * A hint indicating what the language of the result of dereferencing the link should be. example:
   * en
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "hreflang")
  @XmlElement(name = "hreflang")
  private String hreflang;

  /**
   * Used to label the destination of a link such that it can be used as a human-readable
   * identifier. example: "Trierer Strasse 70, 53115 Bonn"
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "title")
  @XmlElement(name = "title")
  private String title;

  /**
   * undocumented in spec.
   *
   * <p>This probably shouldn't be used because its not defined.
   */
  @JsonInclude(Include.NON_DEFAULT)
  @XmlElementWrapper(name = "length")
  @XmlElement(name = "length")
  private Integer length;

  /**
   * Date of creation of the resource pointed to by the link. format: date-time
   *
   * <p>This is only is one of the link specifications.  This probably shouldn't be used.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "created")
  @XmlElement(name = "created")
  private String created;

  /**
   * description: Most recent date on which the resource pointed to by the link was changed. format:
   * date-time
   *
   * <p>This is only is one of the link specifications.  This probably shouldn't be used.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "updated")
  @XmlElement(name = "updated")
  private String updated;

  /**
   * example: http://data.example.com/buildings/123
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "href")
  @XmlElement(name = "href")
  private String href;


  public OgcApiLink() {

  }

  public OgcApiLink(String href, String type) {
    this.href = href;
    this.type = type;
  }


  public String getRel() {
    return rel;
  }

  public void setRel(String rel) {
    this.rel = rel;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getHreflang() {
    return hreflang;
  }

  public void setHreflang(String hreflang) {
    this.hreflang = hreflang;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
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

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OgcApiLink link = (OgcApiLink) o;
    return Objects.equals(this.href, link.getHref())
        && Objects.equals(this.rel, link.getRel())
        && Objects.equals(this.type, link.getType())
        && Objects.equals(this.hreflang, link.getHreflang())

        && Objects.equals(this.created, link.getCreated())
        && Objects.equals(this.updated, link.getUpdated())
        && Objects.equals(this.length, link.getLength());
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
   * Convert the given object to string with each line indented by 4 spaces (except the first
   * line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  public OgcApiLink href(String s) {
    setHref(s);
    return this;
  }

  public OgcApiLink title(String s) {
    setTitle(s);
    return this;
  }

  public OgcApiLink rel(String s) {
    setRel(s);
    return this;
  }

  public OgcApiLink type(String s) {
    setType(s);
    return this;
  }
}
