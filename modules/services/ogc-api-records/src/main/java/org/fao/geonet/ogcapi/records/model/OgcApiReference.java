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
 * OgcApiReference.
 * cf https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/schema.yaml
 */
public class OgcApiReference {

  /**
   *  format: uri-reference.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "ref")
  @XmlElement(name = "ref$")
  public String ref;

  //------------------------------------------------


  public String getRef() {
    return ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }
}
