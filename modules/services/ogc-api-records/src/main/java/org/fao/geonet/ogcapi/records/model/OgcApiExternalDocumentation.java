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
 * OgcApiExternalDocumentation.
 *
 * <p>cf. https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/schema.yaml
 *
 * <p>I haven't included the "x-*" pattern properties.
 */
public class OgcApiExternalDocumentation {

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
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "description")
  @XmlElement(name = "description")
  private String description;

  //------------------------------------------


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
