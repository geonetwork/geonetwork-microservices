/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */


package org.fao.geonet.index.model.rss;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Java class for rss complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="rss">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="channel" type="{}channel"/>
 *       &lt;/sequence>
 *       &lt;attribute name="version" type="{}version" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rss", propOrder = {
    "channel"
})
public class Rss {

  @XmlElement(required = true)
  protected Channel channel;
  @XmlAttribute(name = "version")
  protected String version;

  /**
   * Gets the value of the channel property.
   *
   * @return possible object is {@link Channel }
   */
  public Channel getChannel() {
    return channel;
  }

  /**
   * Sets the value of the channel property.
   *
   * @param value allowed object is {@link Channel }
   */
  public void setChannel(Channel value) {
    this.channel = value;
  }

  /**
   * Gets the value of the version property.
   *
   * @return possible object is {@link String }
   */
  public String getVersion() {
    return version;
  }

  /**
   * Sets the value of the version property.
   *
   * @param value allowed object is {@link String }
   */
  public void setVersion(String value) {
    this.version = value;
  }

}
