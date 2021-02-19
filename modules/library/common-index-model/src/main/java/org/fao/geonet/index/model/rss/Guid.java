/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */


package org.fao.geonet.index.model.rss;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * Java class for guid complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="guid">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="isPermaLink" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "guid", propOrder = {
    "value"
})
public class Guid {

  @XmlValue
  protected String value;
  @XmlAttribute(name = "isPermaLink")
  protected Boolean isPermaLink;

  /**
   * Gets the value of the value property.
   *
   * @return possible object is {@link String }
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets the value of the value property.
   *
   * @param value allowed object is {@link String }
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Gets the value of the isPermaLink property.
   *
   * @return possible object is {@link Boolean }
   */
  public Boolean isIsPermaLink() {
    return isPermaLink;
  }

  /**
   * Sets the value of the isPermaLink property.
   *
   * @param value allowed object is {@link Boolean }
   */
  public void setIsPermaLink(Boolean value) {
    this.isPermaLink = value;
  }

}
