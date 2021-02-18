/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */


package org.fao.geonet.index.model.rss;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * Java class for cloud complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="cloud">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="domain" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="port">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *             &lt;maxInclusive value="65536"/>
 *             &lt;minInclusive value="0"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="path" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="registerProcedure" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="protocol" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cloud")
public class Cloud {

  @XmlAttribute(name = "domain")
  protected String domain;
  @XmlAttribute(name = "port")
  protected Integer port;
  @XmlAttribute(name = "path")
  protected String path;
  @XmlAttribute(name = "registerProcedure")
  protected String registerProcedure;
  @XmlAttribute(name = "protocol")
  protected String protocol;

  /**
   * Gets the value of the domain property.
   *
   * @return possible object is {@link String }
   */
  public String getDomain() {
    return domain;
  }

  /**
   * Sets the value of the domain property.
   *
   * @param value allowed object is {@link String }
   */
  public void setDomain(String value) {
    this.domain = value;
  }

  /**
   * Gets the value of the port property.
   *
   * @return possible object is {@link Integer }
   */
  public Integer getPort() {
    return port;
  }

  /**
   * Sets the value of the port property.
   *
   * @param value allowed object is {@link Integer }
   */
  public void setPort(Integer value) {
    this.port = value;
  }

  /**
   * Gets the value of the path property.
   *
   * @return possible object is {@link String }
   */
  public String getPath() {
    return path;
  }

  /**
   * Sets the value of the path property.
   *
   * @param value allowed object is {@link String }
   */
  public void setPath(String value) {
    this.path = value;
  }

  /**
   * Gets the value of the registerProcedure property.
   *
   * @return possible object is {@link String }
   */
  public String getRegisterProcedure() {
    return registerProcedure;
  }

  /**
   * Sets the value of the registerProcedure property.
   *
   * @param value allowed object is {@link String }
   */
  public void setRegisterProcedure(String value) {
    this.registerProcedure = value;
  }

  /**
   * Gets the value of the protocol property.
   *
   * @return possible object is {@link String }
   */
  public String getProtocol() {
    return protocol;
  }

  /**
   * Sets the value of the protocol property.
   *
   * @param value allowed object is {@link String }
   */
  public void setProtocol(String value) {
    this.protocol = value;
  }

}
