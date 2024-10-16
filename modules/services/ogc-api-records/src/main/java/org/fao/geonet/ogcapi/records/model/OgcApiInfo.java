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
 * cf https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/schema.yaml
 *
 * <p>I haven't included the "x-*" pattern properties.
 */
public class OgcApiInfo {

  /**
   * undefined in spec.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "title")
  @XmlElement(name = "title")
  private String title;

  /**
   * undefined in spec.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "description")
  @XmlElement(name = "description")
  private String description;

  /**
   * undefined in spec.
   *
   * <p>format: uri-reference
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "termsOfService")
  @XmlElement(name = "termsOfService")
  private String termsOfService;

  /**
   * undefined in spec.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "contact")
  @XmlElement(name = "contact")
  private OgcApiSchemaContact contact;


  /**
   * undefined in spec.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "license")
  @XmlElement(name = "license")
  private OgcApiLicense license;

  /**
   * undefined in spec.
   *
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "version")
  @XmlElement(name = "version")
  private String version;
}
