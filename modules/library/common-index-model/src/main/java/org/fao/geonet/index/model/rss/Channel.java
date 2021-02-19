/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */


package org.fao.geonet.index.model.rss;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Java class for channel complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="channel">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="link" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="copyright" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="managingEditor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="webMaster" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pubDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lastBuildDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="category" type="{}category" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="generator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="docs" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cloud" type="{}cloud" minOccurs="0"/>
 *         &lt;element name="ttl" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="image" type="{}image" minOccurs="0"/>
 *         &lt;element name="rating" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="textInput" type="{}textInput" minOccurs="0"/>
 *         &lt;element name="skipHours" type="{}skipHours" minOccurs="0"/>
 *         &lt;element name="skipDays" type="{}skipDays" minOccurs="0"/>
 *         &lt;element name="item" type="{}item" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "channel", propOrder = {
    "title",
    "link",
    "description",
    "language",
    "copyright",
    "managingEditor",
    "webMaster",
    "pubDate",
    "lastBuildDate",
    "category",
    "generator",
    "docs",
    "cloud",
    "ttl",
    "image",
    "rating",
    "textInput",
    "skipHours",
    "skipDays",
    "item"
})
public class Channel {

  @XmlElement(required = true)
  protected String title;
  @XmlElement(required = true)
  protected String link;
  @XmlElement(required = true)
  protected String description;
  protected String language;
  protected String copyright;
  protected String managingEditor;
  protected String webMaster;
  protected String pubDate;
  protected String lastBuildDate;
  protected List<Category> category;
  protected String generator;
  protected String docs;
  protected Cloud cloud;
  protected Integer ttl;
  protected Image image;
  protected String rating;
  protected TextInput textInput;
  protected SkipHours skipHours;
  protected SkipDays skipDays;
  protected List<Item> item;

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
   * Gets the value of the language property.
   *
   * @return possible object is {@link String }
   */
  public String getLanguage() {
    return language;
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
   * Gets the value of the copyright property.
   *
   * @return possible object is {@link String }
   */
  public String getCopyright() {
    return copyright;
  }

  /**
   * Sets the value of the copyright property.
   *
   * @param value allowed object is {@link String }
   */
  public void setCopyright(String value) {
    this.copyright = value;
  }

  /**
   * Gets the value of the managingEditor property.
   *
   * @return possible object is {@link String }
   */
  public String getManagingEditor() {
    return managingEditor;
  }

  /**
   * Sets the value of the managingEditor property.
   *
   * @param value allowed object is {@link String }
   */
  public void setManagingEditor(String value) {
    this.managingEditor = value;
  }

  /**
   * Gets the value of the webMaster property.
   *
   * @return possible object is {@link String }
   */
  public String getWebMaster() {
    return webMaster;
  }

  /**
   * Sets the value of the webMaster property.
   *
   * @param value allowed object is {@link String }
   */
  public void setWebMaster(String value) {
    this.webMaster = value;
  }

  /**
   * Gets the value of the pubDate property.
   *
   * @return possible object is {@link String }
   */
  public String getPubDate() {
    return pubDate;
  }

  /**
   * Sets the value of the pubDate property.
   *
   * @param value allowed object is {@link String }
   */
  public void setPubDate(String value) {
    this.pubDate = value;
  }

  /**
   * Gets the value of the lastBuildDate property.
   *
   * @return possible object is {@link String }
   */
  public String getLastBuildDate() {
    return lastBuildDate;
  }

  /**
   * Sets the value of the lastBuildDate property.
   *
   * @param value allowed object is {@link String }
   */
  public void setLastBuildDate(String value) {
    this.lastBuildDate = value;
  }

  /**
   * Gets the value of the category property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the category property.
   *
   * <p>For example, to add a new item, do as follows:
   * <pre>
   *    getCategory().add(newItem);
   * </pre>
   *
   *
   * <p>Objects of the following type(s) are allowed in the list {@link Category }
   */
  public List<Category> getCategory() {
    if (category == null) {
      category = new ArrayList<Category>();
    }
    return this.category;
  }

  /**
   * Gets the value of the generator property.
   *
   * @return possible object is {@link String }
   */
  public String getGenerator() {
    return generator;
  }

  /**
   * Sets the value of the generator property.
   *
   * @param value allowed object is {@link String }
   */
  public void setGenerator(String value) {
    this.generator = value;
  }

  /**
   * Gets the value of the docs property.
   *
   * @return possible object is {@link String }
   */
  public String getDocs() {
    return docs;
  }

  /**
   * Sets the value of the docs property.
   *
   * @param value allowed object is {@link String }
   */
  public void setDocs(String value) {
    this.docs = value;
  }

  /**
   * Gets the value of the cloud property.
   *
   * @return possible object is {@link Cloud }
   */
  public Cloud getCloud() {
    return cloud;
  }

  /**
   * Sets the value of the cloud property.
   *
   * @param value allowed object is {@link Cloud }
   */
  public void setCloud(Cloud value) {
    this.cloud = value;
  }

  /**
   * Gets the value of the ttl property.
   *
   * @return possible object is {@link Integer }
   */
  public Integer getTtl() {
    return ttl;
  }

  /**
   * Sets the value of the ttl property.
   *
   * @param value allowed object is {@link Integer }
   */
  public void setTtl(Integer value) {
    this.ttl = value;
  }

  /**
   * Gets the value of the image property.
   *
   * @return possible object is {@link Image }
   */
  public Image getImage() {
    return image;
  }

  /**
   * Sets the value of the image property.
   *
   * @param value allowed object is {@link Image }
   */
  public void setImage(Image value) {
    this.image = value;
  }

  /**
   * Gets the value of the rating property.
   *
   * @return possible object is {@link String }
   */
  public String getRating() {
    return rating;
  }

  /**
   * Sets the value of the rating property.
   *
   * @param value allowed object is {@link String }
   */
  public void setRating(String value) {
    this.rating = value;
  }

  /**
   * Gets the value of the textInput property.
   *
   * @return possible object is {@link TextInput }
   */
  public TextInput getTextInput() {
    return textInput;
  }

  /**
   * Sets the value of the textInput property.
   *
   * @param value allowed object is {@link TextInput }
   */
  public void setTextInput(TextInput value) {
    this.textInput = value;
  }

  /**
   * Gets the value of the skipHours property.
   *
   * @return possible object is {@link SkipHours }
   */
  public SkipHours getSkipHours() {
    return skipHours;
  }

  /**
   * Sets the value of the skipHours property.
   *
   * @param value allowed object is {@link SkipHours }
   */
  public void setSkipHours(SkipHours value) {
    this.skipHours = value;
  }

  /**
   * Gets the value of the skipDays property.
   *
   * @return possible object is {@link SkipDays }
   */
  public SkipDays getSkipDays() {
    return skipDays;
  }

  /**
   * Sets the value of the skipDays property.
   *
   * @param value allowed object is {@link SkipDays }
   */
  public void setSkipDays(SkipDays value) {
    this.skipDays = value;
  }

  /**
   * Gets the value of the item property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the item property.
   *
   * <p>For example, to add a new item, do as follows:
   * <pre>
   *    getItem().add(newItem);
   * </pre>
   *
   *
   * <p>Objects of the following type(s) are allowed in the list {@link Item }
   */
  public List<Item> getItem() {
    if (item == null) {
      item = new ArrayList<Item>();
    }
    return this.item;
  }

}
