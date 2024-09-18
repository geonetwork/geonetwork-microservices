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
 * This is different (simpler) from the "normal" OgcApiContact.
 *
 * <p>cf. https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/schema.yaml
 *
 * <p>I haven't included the "x-*" pattern properties.
 */
public class OgcApiSchemaContact {

  /**
   * undefined in spec.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "name")
  @XmlElement(name = "name")
  private String name;

  /**
   * undefined in spec.
   *
   * <p>format: uri-reference
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "url")
  @XmlElement(name = "url")
  private String url;

  /**
   * undefined in spec.
   *
   * <p>format: email
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "email")
  @XmlElement(name = "email")
  private String email;

  //-----------------------------------------------------


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
