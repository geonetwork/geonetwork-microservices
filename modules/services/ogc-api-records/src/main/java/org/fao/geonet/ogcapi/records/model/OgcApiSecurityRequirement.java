/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * OgcApiSecurityRequirement.
 *
 * <p>https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/schema.yaml
 */
public class OgcApiSecurityRequirement {

  /**
   * undefined in spec.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "items")
  @XmlElement(name = "items")
  private List<String> items;

  //------------------------------------------


  public List<String> getItems() {
    return items;
  }

  public void setItems(List<String> items) {
    this.items = items;
  }
}
