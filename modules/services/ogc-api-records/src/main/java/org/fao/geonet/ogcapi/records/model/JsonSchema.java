package org.fao.geonet.ogcapi.records.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * This is for Queryables.  Its a json schema. cf https://json-schema.org/draft/2020-12/schema
 *
 * <p>example: https://demo.pycsw.org/gisdata/collections/metadata:main/queryables?f=json
 */
public class JsonSchema {

  /**
   * The property $schema is https://json-schema.org/draft/2020-12/schema.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "$schema")
  @XmlElement(name = "$schema")
  @com.fasterxml.jackson.annotation.JsonProperty("$schema")
  public String schema = "https://json-schema.org/draft/2020-12/schema";

  /**
   * The type is object and each property is a queryable.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "type")
  @XmlElement(name = "type")
  public String type = "object";

  /**
   * The property $id is the URI of the resource without query parameters.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "id")
  @XmlElement(name = "id")
  public String id;

  /**
   * The property $id is the URI of the resource without query parameters.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "title")
  @XmlElement(name = "title")
  public String title = "Queryables for GeoNetwork Collection";

  /**
   * https://json-schema.org/draft/2020-12/schema.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "description")
  @XmlElement(name = "description")
  public String description;


  /**
   * The properties for this schema.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "properties")
  @XmlElement(name = "properties")
  public Map<String, JsonProperty> properties;

  //----------------------------------------------


  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Map<String, JsonProperty> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, JsonProperty> properties) {
    this.properties = properties;
  }
}
