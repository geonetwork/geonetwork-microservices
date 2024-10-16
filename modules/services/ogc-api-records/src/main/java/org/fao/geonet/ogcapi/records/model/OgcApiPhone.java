/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * cf. https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/contact.yaml
 *
 * <p>Represents the Contact's phone number.
 */
@XmlRootElement(name = "phone")
@XmlAccessorType(XmlAccessType.FIELD)
public class OgcApiPhone {

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "value")
  @XmlElement(name = "value")
  private String value;

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "roles")
  @XmlElement(name = "roles")
  private List<String> roles = new ArrayList<>();


  public OgcApiPhone(String phoneNumber) {
    value = phoneNumber;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }

}
