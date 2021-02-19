/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */


package org.fao.geonet.index.model.rss;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Java classfor item complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="item">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="link" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="author" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="category" type="{}category" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="comments" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enclosure" type="{}enclosure" minOccurs="0"/>
 *         &lt;element name="guid" type="{}guid" minOccurs="0"/>
 *         &lt;element name="pubDate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="source" type="{}source" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "item", propOrder = {
    "title",
    "link",
    "description",
    "author",
    "category",
    "comments",
    "enclosure",
    "guid",
    "pubDate",
    "source"
})
public class Item {

  protected String title;
  protected String link;
  protected String description;
  protected String author;
  protected List<Category> category;
  protected String comments;
  protected Enclosure enclosure;
  protected Guid guid;
  protected String pubDate;
  protected Source source;

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
   * Gets the value of the author property.
   *
   * @return possible object is {@link String }
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Sets the value of the author property.
   *
   * @param value allowed object is {@link String }
   */
  public void setAuthor(String value) {
    this.author = value;
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
   * Gets the value of the comments property.
   *
   * @return possible object is {@link String }
   */
  public String getComments() {
    return comments;
  }

  /**
   * Sets the value of the comments property.
   *
   * @param value allowed object is {@link String }
   */
  public void setComments(String value) {
    this.comments = value;
  }

  /**
   * Gets the value of the enclosure property.
   *
   * @return possible object is {@link Enclosure }
   */
  public Enclosure getEnclosure() {
    return enclosure;
  }

  /**
   * Sets the value of the enclosure property.
   *
   * @param value allowed object is {@link Enclosure }
   */
  public void setEnclosure(Enclosure value) {
    this.enclosure = value;
  }

  /**
   * Gets the value of the guid property.
   *
   * @return possible object is {@link Guid }
   */
  public Guid getGuid() {
    return guid;
  }

  /**
   * Sets the value of the guid property.
   *
   * @param value allowed object is {@link Guid }
   */
  public void setGuid(Guid value) {
    this.guid = value;
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
   * Gets the value of the source property.
   *
   * @return possible object is {@link Source }
   */
  public Source getSource() {
    return source;
  }

  /**
   * Sets the value of the source property.
   *
   * @param value allowed object is {@link Source }
   */
  public void setSource(Source value) {
    this.source = value;
  }

}
