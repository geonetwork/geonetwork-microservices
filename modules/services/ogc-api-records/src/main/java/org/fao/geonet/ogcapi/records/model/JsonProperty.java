package org.fao.geonet.ogcapi.records.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * This is for Queryables.  Its a json schema. cf https://json-schema.org/draft/2020-12/schema
 *
 * <p>https://json-schema.org/learn/miscellaneous-examples
 *
 * <p>example: https://demo.pycsw.org/gisdata/collections/metadata:main/queryables?f=json
 *
 * <p>THIS IS SIMPLIFIED - SEE FULL SPECIFICATION and JsonItem
 */
public class JsonProperty {

  public static final String TypeString = "string";

  //----------------------

  /**
   * https://json-schema.org/draft/2020-12/schema.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "title")
  @XmlElement(name = "title")
  public String title;

  /**
   * https://json-schema.org/draft/2020-12/schema.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "type")
  @XmlElement(name = "type")
  public String type;

  /**
   * https://json-schema.org/draft/2020-12/schema.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "description")
  @XmlElement(name = "description")
  public String description;

  /**
   * cf. https://docs.ogc.org/is/19-079r2/19-079r2.html.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "format")
  @XmlElement(name = "format")
  public String format;

  /**
   * cf. https://docs.ogc.org/is/19-079r2/19-079r2.html.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "enum")
  @XmlElement(name = "enum")
  @com.fasterxml.jackson.annotation.JsonProperty("enum")
  public List<String> enumeration;

  /**
   * cf. https://docs.ogc.org/is/19-079r2/19-079r2.html.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "x-ogc-role")
  @XmlElement(name = "x-ogc-role")
  @com.fasterxml.jackson.annotation.JsonProperty("x-ogc-role")
  public String xxOgcRole;

  /**
   * cf. https://docs.ogc.org/is/19-079r2/19-079r2.html.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "items")
  @XmlElement(name = "items")
  public JsonItem items;

  //----------------------------------

  /**
   * builds a minimal JsonProperty (part of json schema).
   *
   * @param type  type of the property
   * @param title title of the property
   * @param description description of the property
   */
  public JsonProperty(String type, String title, String description) {
    this.type = type;
    this.title = title;
    this.description = description;
  }

  //----------------------------------


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public List<String> getEnum() {
    return enumeration;
  }

  public void setEnum(List<String> enumeration) {
    this.enumeration = enumeration;
  }

  public String getxOgcRole() {
    return xxOgcRole;
  }

  public void setxOgcRole(String xxOgcRole) {
    this.xxOgcRole = xxOgcRole;
  }

  public JsonItem getItems() {
    return items;
  }

  public void setItems(JsonItem items) {
    this.items = items;
  }
}
