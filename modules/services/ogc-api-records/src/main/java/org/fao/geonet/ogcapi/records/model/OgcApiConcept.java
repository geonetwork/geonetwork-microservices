/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;


/**
 * cf. https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/theme.yaml
 *
 * <p>Entity/concept identifiers from this knowledge
 * system. it is recommended that a resolvable URI be used for each entity/concept identifier.
 */
public class OgcApiConcept {


  /**
   * An identifier for the concept.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "id")
  @XmlElement(name = "id")
  public String id;

  /**
   * A human readable title for the concept.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "title")
  @XmlElement(name = "title")
  public String title;

  /**
   * A human readable description for the concept.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "description")
  @XmlElement(name = "description")
  public String description;

  /**
   * A URI providing further description of the concept. format: uri
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "url")
  @XmlElement(name = "url")
  public String url;

  public OgcApiConcept(String id, String url) {
    this.id = id;
    this.url = url;
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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
