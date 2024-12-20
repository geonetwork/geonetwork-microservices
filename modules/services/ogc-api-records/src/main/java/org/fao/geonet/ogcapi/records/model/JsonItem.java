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
 * THIS IS SIMPLIFIED - SEE FULL SPECIFICATION.
 *
 * <p>See the JSON Schema specification.
 */
public class JsonItem {

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "type")
  @XmlElement(name = "type")
  public String type;

  //---------------------------------------------


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
