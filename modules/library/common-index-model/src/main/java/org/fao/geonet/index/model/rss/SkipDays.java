/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */


package org.fao.geonet.index.model.rss;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Java class for skipDays complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="skipDays">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="day" maxOccurs="7">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Monday"/>
 *               &lt;enumeration value="Tuesday"/>
 *               &lt;enumeration value="Wednesday"/>
 *               &lt;enumeration value="Thursday"/>
 *               &lt;enumeration value="Friday"/>
 *               &lt;enumeration value="Saturday"/>
 *               &lt;enumeration value="Sunday"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "skipDays", propOrder = {
    "day"
})
public class SkipDays {

  @XmlElement(required = true)
  protected List<String> day;

  /**
   * Gets the value of the day property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the day property.
   *
   * <p>For example, to add a new item, do as follows:
   * <pre>
   *    getDay().add(newItem);
   * </pre>
   *
   *
   * <p>Objects of the following type(s) are allowed in the list {@link String }
   */
  public List<String> getDay() {
    if (day == null) {
      day = new ArrayList<String>();
    }
    return this.day;
  }

}
