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
 * OgcApiTag.
 *
 * <p>cf. https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/schema.yaml
 *
 * <p>I haven't included the "x-*" pattern properties.
 */
public class OgcApiTag {

  /**
   * undefined in spec.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "name")
  @XmlElement(name = "name")
  private String name;

  /**
   * undefined in spec.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "description")
  @XmlElement(name = "description")
  private String description;

  /**
   * undefined in spec.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "externalDocs")
  @XmlElement(name = "externalDocs")
  private OgcApiExternalDocumentation externalDocs;

}
