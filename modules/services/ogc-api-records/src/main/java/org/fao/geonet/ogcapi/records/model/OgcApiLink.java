/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/linkBase.yaml
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
   */
  @JsonInclude(Include.NON_DEFAULT)
  @XmlElementWrapper(name = "length")
  @XmlElement(name = "length")
  private Integer length;

  /**
   * Date of creation of the resource pointed to by the link. format: date-time
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "created")
  @XmlElement(name = "created")
  private String created;

  /**
   * description: Most recent date on which the resource pointed to by the link was changed. format:
   * date-time
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
}
