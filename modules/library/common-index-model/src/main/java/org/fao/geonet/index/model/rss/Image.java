/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */


package org.fao.geonet.index.model.rss;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Java classfor image complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="image">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="link" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="width" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minExclusive value="0"/>
 *               &lt;maxInclusive value="144"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="height" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minExclusive value="0"/>
 *               &lt;maxInclusive value="400"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "image", propOrder = {
    "url",
    "title",
    "link",
    "width",
    "height",
    "description"
})
public class Image {

  @XmlElement(required = true)
  protected String url;
  @XmlElement(required = true)
  protected String title;
  @XmlElement(required = true)
  protected String link;
  @XmlElement(defaultValue = "88")
  protected Integer width;
  @XmlElement(defaultValue = "31")
  protected Integer height;
  protected String description;

  /**
   * Gets the value of the url property.
   *
   * @return possible object is {@link String }
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets the value of the url property.
   *
   * @param value allowed object is {@link String }
   */
  public void setUrl(String value) {
    this.url = value;
  }

  /**
   * Gets the value of the title property.
   *
   * @return possible object is {@link String }
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the value of the title property.
   *
   * @param value allowed object is {@link String }
   */
  public void setTitle(String value) {
    this.title = value;
  }

  /**
   * Gets the value of the link property.
   *
   * @return possible object is {@link String }
   */
  public String getLink() {
    return link;
  }

  /**
   * Sets the value of the link property.
   *
   * @param value allowed object is {@link String }
   */
  public void setLink(String value) {
    this.link = value;
  }

  /**
   * Gets the value of the width property.
   *
   * @return possible object is {@link Integer }
   */
  public Integer getWidth() {
    return width;
  }

  /**
   * Sets the value of the width property.
   *
   * @param value allowed object is {@link Integer }
   */
  public void setWidth(Integer value) {
    this.width = value;
  }

  /**
   * Gets the value of the height property.
   *
   * @return possible object is {@link Integer }
   */
  public Integer getHeight() {
    return height;
  }

  /**
   * Sets the value of the height property.
   *
   * @param value allowed object is {@link Integer }
   */
  public void setHeight(Integer value) {
    this.height = value;
  }

  /**
   * Gets the value of the description property.
   *
   * @return possible object is {@link String }
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the value of the description property.
   *
   * @param value allowed object is {@link String }
   */
  public void setDescription(String value) {
    this.description = value;
  }

}
