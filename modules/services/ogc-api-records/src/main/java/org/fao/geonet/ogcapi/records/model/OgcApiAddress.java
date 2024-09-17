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
 * see "address" in https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/contact.yaml
 *
 * <p>Physical location at which contact can be made.
 */
@XmlRootElement(name = "address")
@XmlAccessorType(XmlAccessType.FIELD)
public class OgcApiAddress {

  /**
   * Address lines for the location.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "deliveryPoint")
  @XmlElement(name = "deliveryPoint")
  public List<String> deliveryPoint = new ArrayList<>();

  /**
   * City for the location.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "city")
  @XmlElement(name = "city")
  public String city;

  /**
   * State or province of the location.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "administrativeArea")
  @XmlElement(name = "administrativeArea")
  public String administrativeArea;

  /**
   * ZIP or other postal code.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "postalCode")
  @XmlElement(name = "postalCode")
  public String postalCode;

  /**
   * Country of the physical address.  ISO 3166-1 is recommended.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "country")
  @XmlElement(name = "country")
  public String country;

  /**
   * The type of address (e.g. office, home, etc.).
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "roles")
  @XmlElement(name = "roles")
  public List<String> roles = new ArrayList<>();

  public OgcApiAddress(String address) {
    deliveryPoint.add(address);
  }

  public List<String> getDeliveryPoint() {
    return deliveryPoint;
  }

  public void setDeliveryPoint(List<String> deliveryPoint) {
    this.deliveryPoint = deliveryPoint;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getAdministrativeArea() {
    return administrativeArea;
  }

  public void setAdministrativeArea(String administrativeArea) {
    this.administrativeArea = administrativeArea;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }
}
