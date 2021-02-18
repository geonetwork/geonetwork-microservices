/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.opensearch;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.namespace.QName;


/**
 * Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ShortName">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="16"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Description">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1024"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Url" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="template" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="indexOffset" default="1">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="pageOffset" default="1">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;anyAttribute processContents='lax'/>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Contact" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Tags" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="256"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="LongName" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="48"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Image" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="height" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *                 &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *                 &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Query" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="role" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="request"/>
 *                       &lt;enumeration value="example"/>
 *                       &lt;enumeration value="related"/>
 *                       &lt;enumeration value="correction"/>
 *                       &lt;enumeration value="subset"/>
 *                       &lt;enumeration value="superset"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="title">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;maxLength value="256"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="totalResults">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger">
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="searchTerms" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *                 &lt;attribute name="count">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger">
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="startIndex">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="startPage">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="language" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" default="*" />
 *                 &lt;attribute name="inputEncoding" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" default="UTF-8" />
 *                 &lt;attribute name="outputEncoding" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" default="UTF-8" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Developer" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="64"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Attribution" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="256"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="SyndicationRight" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="open"/>
 *               &lt;enumeration value="limited"/>
 *               &lt;enumeration value="private"/>
 *               &lt;enumeration value="closed"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="AdultContent" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Language" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="InputEncoding" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="OutputEncoding" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "shortName",
    "description",
    "url",
    "contact",
    "tags",
    "longName",
    "image",
    "query",
    "developer",
    "attribution",
    "syndicationRight",
    "adultContent",
    "language",
    "inputEncoding",
    "outputEncoding"
})

@XmlRootElement(
    name = "OpenSearchDescription")
public class OpenSearchDescription {

  @XmlElement(name = "ShortName", required = true)
  protected String shortName;
  @XmlElement(name = "Description", required = true)
  protected String description;
  @XmlElement(name = "Url", required = true)
  protected List<OpenSearchDescription.Url> url;
  @XmlElement(name = "Contact")
  protected String contact;
  @XmlElement(name = "Tags")
  protected String tags;
  @XmlElement(name = "LongName")
  protected String longName;
  @XmlElement(name = "Image")
  protected List<OpenSearchDescription.Image> image;
  @XmlElement(name = "Query")
  protected List<OpenSearchDescription.Query> query;
  @XmlElement(name = "Developer")
  protected String developer;
  @XmlElement(name = "Attribution")
  protected String attribution;
  @XmlElement(name = "SyndicationRight", defaultValue = "open")
  protected String syndicationRight;
  @XmlElement(name = "AdultContent", defaultValue = "false")
  protected String adultContent;
  @XmlElement(name = "Language", defaultValue = "*")
  protected List<String> language;
  @XmlElement(name = "InputEncoding", defaultValue = "UTF-8")
  protected List<Object> inputEncoding;
  @XmlElement(name = "OutputEncoding", defaultValue = "UTF-8")
  protected List<Object> outputEncoding;

  /**
   * Gets the value of the shortName property.
   *
   * @return possible object is {@link String }
   */
  public String getShortName() {
    return shortName;
  }

  /**
   * Sets the value of the shortName property.
   *
   * @param value allowed object is {@link String }
   */
  public void setShortName(String value) {
    this.shortName = value;
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

  /**
   * Gets the value of the url property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the url property.
   *
   * <p>For example, to add a new item, do as follows:
   * <pre>
   *    getUrl().add(newItem);
   * </pre>
   *
   *
   * <p>Objects of the following type(s) are allowed in the list {@link OpenSearchDescription.Url }
   */
  public List<OpenSearchDescription.Url> getUrl() {
    if (url == null) {
      url = new ArrayList<OpenSearchDescription.Url>();
    }
    return this.url;
  }

  /**
   * Gets the value of the contact property.
   *
   * @return possible object is {@link String }
   */
  public String getContact() {
    return contact;
  }

  /**
   * Sets the value of the contact property.
   *
   * @param value allowed object is {@link String }
   */
  public void setContact(String value) {
    this.contact = value;
  }

  /**
   * Gets the value of the tags property.
   *
   * @return possible object is {@link String }
   */
  public String getTags() {
    return tags;
  }

  /**
   * Sets the value of the tags property.
   *
   * @param value allowed object is {@link String }
   */
  public void setTags(String value) {
    this.tags = value;
  }

  /**
   * Gets the value of the longName property.
   *
   * @return possible object is {@link String }
   */
  public String getLongName() {
    return longName;
  }

  /**
   * Sets the value of the longName property.
   *
   * @param value allowed object is {@link String }
   */
  public void setLongName(String value) {
    this.longName = value;
  }

  /**
   * Gets the value of the image property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the image property.
   *
   * <p>For example, to add a new item, do as follows:
   * <pre>
   *    getImage().add(newItem);
   * </pre>
   *
   *
   * <p>Objects of the following type(s) are allowed in
   * the list {@link OpenSearchDescription.Image }
   */
  public List<OpenSearchDescription.Image> getImage() {
    if (image == null) {
      image = new ArrayList<OpenSearchDescription.Image>();
    }
    return this.image;
  }

  /**
   * Gets the value of the query property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the query property.
   *
   * <p>For example, to add a new item, do as follows:
   * <pre>
   *    getQuery().add(newItem);
   * </pre>
   *
   *
   * <p>Objects of the following type(s) are allowed
   * in the list {@link OpenSearchDescription.Query }
   */
  public List<OpenSearchDescription.Query> getQuery() {
    if (query == null) {
      query = new ArrayList<OpenSearchDescription.Query>();
    }
    return this.query;
  }

  /**
   * Gets the value of the developer property.
   *
   * @return possible object is {@link String }
   */
  public String getDeveloper() {
    return developer;
  }

  /**
   * Sets the value of the developer property.
   *
   * @param value allowed object is {@link String }
   */
  public void setDeveloper(String value) {
    this.developer = value;
  }

  /**
   * Gets the value of the attribution property.
   *
   * @return possible object is {@link String }
   */
  public String getAttribution() {
    return attribution;
  }

  /**
   * Sets the value of the attribution property.
   *
   * @param value allowed object is {@link String }
   */
  public void setAttribution(String value) {
    this.attribution = value;
  }

  /**
   * Gets the value of the syndicationRight property.
   *
   * @return possible object is {@link String }
   */
  public String getSyndicationRight() {
    return syndicationRight;
  }

  /**
   * Sets the value of the syndicationRight property.
   *
   * @param value allowed object is {@link String }
   */
  public void setSyndicationRight(String value) {
    this.syndicationRight = value;
  }

  /**
   * Gets the value of the adultContent property.
   *
   * @return possible object is {@link String }
   */
  public String getAdultContent() {
    return adultContent;
  }

  /**
   * Sets the value of the adultContent property.
   *
   * @param value allowed object is {@link String }
   */
  public void setAdultContent(String value) {
    this.adultContent = value;
  }

  /**
   * Gets the value of the language property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the language property.
   *
   * <p>For example, to add a new item, do as follows:
   * <pre>
   *    getLanguage().add(newItem);
   * </pre>
   *
   *
   * <p>Objects of the following type(s) are allowed in the list {@link String }
   */
  public List<String> getLanguage() {
    if (language == null) {
      language = new ArrayList<String>();
    }
    return this.language;
  }

  /**
   * Gets the value of the inputEncoding property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the inputEncoding property.
   *
   * <p>For example, to add a new item, do as follows:
   * <pre>
   *    getInputEncoding().add(newItem);
   * </pre>
   *
   *
   * <p>Objects of the following type(s) are allowed in the list {@link Object }
   */
  public List<Object> getInputEncoding() {
    if (inputEncoding == null) {
      inputEncoding = new ArrayList<Object>();
    }
    return this.inputEncoding;
  }

  /**
   * Gets the value of the outputEncoding property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the outputEncoding property.
   *
   * <p>For example, to add a new item, do as follows:
   * <pre>
   *    getOutputEncoding().add(newItem);
   * </pre>
   *
   *
   * <p>Objects of the following type(s) are allowed in the list {@link Object }
   */
  public List<Object> getOutputEncoding() {
    if (outputEncoding == null) {
      outputEncoding = new ArrayList<Object>();
    }
    return this.outputEncoding;
  }


  /**
   * Java class for anonymous complex type.
   *
   * <p>The following schema fragment specifies the expected content contained within this class.
   *
   * <pre>
   * &lt;complexType>
   *   &lt;simpleContent>
   *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
   *       &lt;attribute name="height" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
   *       &lt;attribute name="width" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
   *       &lt;attribute name="type" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
   *     &lt;/extension>
   *   &lt;/simpleContent>
   * &lt;/complexType>
   * </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "", propOrder = {
      "value"
  })
  public static class Image {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "height")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger height;
    @XmlAttribute(name = "width")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger width;
    @XmlAttribute(name = "type")
    @XmlSchemaType(name = "anySimpleType")
    protected String type;

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
     * Gets the value of the height property.
     *
     * @return possible object is {@link BigInteger }
     */
    public BigInteger getHeight() {
      return height;
    }

    /**
     * Sets the value of the height property.
     *
     * @param value allowed object is {@link BigInteger }
     */
    public void setHeight(BigInteger value) {
      this.height = value;
    }

    /**
     * Gets the value of the width property.
     *
     * @return possible object is {@link BigInteger }
     */
    public BigInteger getWidth() {
      return width;
    }

    /**
     * Sets the value of the width property.
     *
     * @param value allowed object is {@link BigInteger }
     */
    public void setWidth(BigInteger value) {
      this.width = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return possible object is {@link String }
     */
    public String getType() {
      return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is {@link String }
     */
    public void setType(String value) {
      this.type = value;
    }

  }


  /**
   * Java class for anonymous complex type.
   *
   * <p>The following schema fragment specifies the expected content contained within this class.
   *
   * <pre>
   * &lt;complexType>
   *   &lt;complexContent>
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
   *       &lt;attribute name="role" use="required">
   *         &lt;simpleType>
   *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
   *             &lt;enumeration value="request"/>
   *             &lt;enumeration value="example"/>
   *             &lt;enumeration value="related"/>
   *             &lt;enumeration value="correction"/>
   *             &lt;enumeration value="subset"/>
   *             &lt;enumeration value="superset"/>
   *           &lt;/restriction>
   *         &lt;/simpleType>
   *       &lt;/attribute>
   *       &lt;attribute name="title">
   *         &lt;simpleType>
   *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
   *             &lt;maxLength value="256"/>
   *           &lt;/restriction>
   *         &lt;/simpleType>
   *       &lt;/attribute>
   *       &lt;attribute name="totalResults">
   *         &lt;simpleType>
   *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger">
   *           &lt;/restriction>
   *         &lt;/simpleType>
   *       &lt;/attribute>
   *       &lt;attribute name="searchTerms" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
   *       &lt;attribute name="count">
   *         &lt;simpleType>
   *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger">
   *           &lt;/restriction>
   *         &lt;/simpleType>
   *       &lt;/attribute>
   *       &lt;attribute name="startIndex">
   *         &lt;simpleType>
   *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
   *           &lt;/restriction>
   *         &lt;/simpleType>
   *       &lt;/attribute>
   *       &lt;attribute name="startPage">
   *         &lt;simpleType>
   *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
   *           &lt;/restriction>
   *         &lt;/simpleType>
   *       &lt;/attribute>
   *       &lt;attribute name="language" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" default="*" />
   *       &lt;attribute name="inputEncoding" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" default="UTF-8" />
   *       &lt;attribute name="outputEncoding" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" default="UTF-8" />
   *     &lt;/restriction>
   *   &lt;/complexContent>
   * &lt;/complexType>
   * </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "")
  public static class Query {

    @XmlAttribute(name = "role", required = true)
    protected String role;
    @XmlAttribute(name = "title")
    protected String title;
    @XmlAttribute(name = "totalResults")
    protected BigInteger totalResults;
    @XmlAttribute(name = "searchTerms")
    @XmlSchemaType(name = "anySimpleType")
    protected String searchTerms;
    @XmlAttribute(name = "count")
    protected BigInteger count;
    @XmlAttribute(name = "startIndex")
    protected BigInteger startIndex;
    @XmlAttribute(name = "startPage")
    protected BigInteger startPage;
    @XmlAttribute(name = "language")
    @XmlSchemaType(name = "anySimpleType")
    protected String language;
    @XmlAttribute(name = "inputEncoding")
    @XmlSchemaType(name = "anySimpleType")
    protected String inputEncoding;
    @XmlAttribute(name = "outputEncoding")
    @XmlSchemaType(name = "anySimpleType")
    protected String outputEncoding;

    /**
     * Gets the value of the role property.
     *
     * @return possible object is {@link String }
     */
    public String getRole() {
      return role;
    }

    /**
     * Sets the value of the role property.
     *
     * @param value allowed object is {@link String }
     */
    public void setRole(String value) {
      this.role = value;
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
     * Gets the value of the totalResults property.
     *
     * @return possible object is {@link BigInteger }
     */
    public BigInteger getTotalResults() {
      return totalResults;
    }

    /**
     * Sets the value of the totalResults property.
     *
     * @param value allowed object is {@link BigInteger }
     */
    public void setTotalResults(BigInteger value) {
      this.totalResults = value;
    }

    /**
     * Gets the value of the searchTerms property.
     *
     * @return possible object is {@link String }
     */
    public String getSearchTerms() {
      return searchTerms;
    }

    /**
     * Sets the value of the searchTerms property.
     *
     * @param value allowed object is {@link String }
     */
    public void setSearchTerms(String value) {
      this.searchTerms = value;
    }

    /**
     * Gets the value of the count property.
     *
     * @return possible object is {@link BigInteger }
     */
    public BigInteger getCount() {
      return count;
    }

    /**
     * Sets the value of the count property.
     *
     * @param value allowed object is {@link BigInteger }
     */
    public void setCount(BigInteger value) {
      this.count = value;
    }

    /**
     * Gets the value of the startIndex property.
     *
     * @return possible object is {@link BigInteger }
     */
    public BigInteger getStartIndex() {
      return startIndex;
    }

    /**
     * Sets the value of the startIndex property.
     *
     * @param value allowed object is {@link BigInteger }
     */
    public void setStartIndex(BigInteger value) {
      this.startIndex = value;
    }

    /**
     * Gets the value of the startPage property.
     *
     * @return possible object is {@link BigInteger }
     */
    public BigInteger getStartPage() {
      return startPage;
    }

    /**
     * Sets the value of the startPage property.
     *
     * @param value allowed object is {@link BigInteger }
     */
    public void setStartPage(BigInteger value) {
      this.startPage = value;
    }

    /**
     * Gets the value of the language property.
     *
     * @return possible object is {@link String }
     */
    public String getLanguage() {
      if (language == null) {
        return "*";
      } else {
        return language;
      }
    }

    /**
     * Sets the value of the language property.
     *
     * @param value allowed object is {@link String }
     */
    public void setLanguage(String value) {
      this.language = value;
    }

    /**
     * Gets the value of the inputEncoding property.
     *
     * @return possible object is {@link String }
     */
    public String getInputEncoding() {
      if (inputEncoding == null) {
        return "UTF-8";
      } else {
        return inputEncoding;
      }
    }

    /**
     * Sets the value of the inputEncoding property.
     *
     * @param value allowed object is {@link String }
     */
    public void setInputEncoding(String value) {
      this.inputEncoding = value;
    }

    /**
     * Gets the value of the outputEncoding property.
     *
     * @return possible object is {@link String }
     */
    public String getOutputEncoding() {
      if (outputEncoding == null) {
        return "UTF-8";
      } else {
        return outputEncoding;
      }
    }

    /**
     * Sets the value of the outputEncoding property.
     *
     * @param value allowed object is {@link String }
     */
    public void setOutputEncoding(String value) {
      this.outputEncoding = value;
    }

  }


  /**
   * Java class for anonymous complex type.
   *
   * <p>The following schema fragment specifies the expected content contained within this class.
   *
   * <pre>
   * &lt;complexType>
   *   &lt;complexContent>
   *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
   *       &lt;attribute name="template" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
   *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
   *       &lt;attribute name="indexOffset" default="1">
   *         &lt;simpleType>
   *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
   *           &lt;/restriction>
   *         &lt;/simpleType>
   *       &lt;/attribute>
   *       &lt;attribute name="pageOffset" default="1">
   *         &lt;simpleType>
   *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
   *           &lt;/restriction>
   *         &lt;/simpleType>
   *       &lt;/attribute>
   *       &lt;anyAttribute processContents='lax'/>
   *     &lt;/restriction>
   *   &lt;/complexContent>
   * &lt;/complexType>
   * </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name = "")
  public static class Url {

    @XmlAnyAttribute
    private final Map<QName, String> otherAttributes = new HashMap<QName, String>();
    @XmlAttribute(name = "template", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String template;
    @XmlAttribute(name = "type", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String type;
    @XmlAttribute(name = "indexOffset")
    protected BigInteger indexOffset;
    @XmlAttribute(name = "pageOffset")
    protected BigInteger pageOffset;

    /**
     * Gets the value of the template property.
     *
     * @return possible object is {@link String }
     */
    public String getTemplate() {
      return template;
    }

    /**
     * Sets the value of the template property.
     *
     * @param value allowed object is {@link String }
     */
    public void setTemplate(String value) {
      this.template = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return possible object is {@link String }
     */
    public String getType() {
      return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value allowed object is {@link String }
     */
    public void setType(String value) {
      this.type = value;
    }

    /**
     * Gets the value of the indexOffset property.
     *
     * @return possible object is {@link BigInteger }
     */
    public BigInteger getIndexOffset() {
      if (indexOffset == null) {
        return new BigInteger("1");
      } else {
        return indexOffset;
      }
    }

    /**
     * Sets the value of the indexOffset property.
     *
     * @param value allowed object is {@link BigInteger }
     */
    public void setIndexOffset(BigInteger value) {
      this.indexOffset = value;
    }

    /**
     * Gets the value of the pageOffset property.
     *
     * @return possible object is {@link BigInteger }
     */
    public BigInteger getPageOffset() {
      if (pageOffset == null) {
        return new BigInteger("1");
      } else {
        return pageOffset;
      }
    }

    /**
     * Sets the value of the pageOffset property.
     *
     * @param value allowed object is {@link BigInteger }
     */
    public void setPageOffset(BigInteger value) {
      this.pageOffset = value;
    }

    /**
     * Gets a map that contains attributes that aren't bound to any typed property on this class.
     *
     * <p>the map is keyed by the name of the attribute and the value is the string value of the
     * attribute.
     *
     * <p>the map returned by this method is live, and you can add new attribute by updating the
     * map
     * directly. Because of this design, there's no setter.
     *
     * @return always non-null
     */
    public Map<QName, String> getOtherAttributes() {
      return otherAttributes;
    }

  }

}
