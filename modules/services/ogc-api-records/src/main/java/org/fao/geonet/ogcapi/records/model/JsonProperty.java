/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
@XmlAccessorType(XmlAccessType.FIELD)
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
  @XmlElement(name = "x-ogc-role")
  @com.fasterxml.jackson.annotation.JsonProperty(value = "x-ogc-role")
  @org.codehaus.jackson.annotate.JsonProperty(value = "x-ogc-role")
  @SuppressWarnings("checkstyle:membername") //defined by ogc, starts with single "x"
  public String xOgcRole;

  /**
   * Where this is in the GeoNetwork Elastic Index -> path to data.
   *
   * <p>List of the places in the elastic index to search for the queryable.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElement(name = "x-gn-elastic")
  @com.fasterxml.jackson.annotation.JsonProperty(value = "x-gn-elastic")
  @org.codehaus.jackson.annotate.JsonProperty(value = "x-gn-elastic")
  @SuppressWarnings("checkstyle:membername") // starts with single "x"
  public List<GnElasticInfo> xGnElastic;

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
   * @param type        type of the property
   * @param title       title of the property
   * @param description description of the property
   */
  public JsonProperty(String type, String title, String description) {
    this.type = type;
    this.title = title;
    this.description = description;
    this.xGnElastic = new ArrayList<>();
  }

  public JsonProperty() {
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
    return xOgcRole;
  }

  @JsonIgnore
  public void setxOgcRole(String xxOgcRole) {
    this.xOgcRole = xxOgcRole;
  }

  public JsonItem getItems() {
    return items;
  }

  public void setItems(JsonItem items) {
    this.items = items;
  }


  public List<GnElasticInfo> getxGnElasticPath() {
    return xGnElastic;
  }

  @JsonIgnore
  public void setxGnElasticPath(List<GnElasticInfo> xxGnElasticPath) {
    this.xGnElastic = xxGnElasticPath;
  }
}
