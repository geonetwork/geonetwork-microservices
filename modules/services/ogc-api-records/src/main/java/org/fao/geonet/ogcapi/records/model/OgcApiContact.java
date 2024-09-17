/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import org.fao.geonet.ogcapi.records.util.CollectionInfoBuilder;
import org.springframework.util.StringUtils;

/**
 * https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/contact.yaml
 */
@XmlRootElement(name = "contact")
@XmlAccessorType(XmlAccessType.FIELD)
public class OgcApiContact {

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "identifier")
  @XmlElement(name = "identifier")
  private String identifier;

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "name")
  @XmlElement(name = "name")
  private String name;

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "position")
  @XmlElement(name = "position")
  private String position;

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "organization")
  @XmlElement(name = "organization")
  private String organization;

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "logo")
  @XmlElement(name = "logo")
  private OgcApiLink logo;

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "")
  @XmlElement(name = "")
  private List<OgcApiPhone> phones = new ArrayList<>();

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "")
  @XmlElement(name = "")
  private List<OgcApiEmail> emails = new ArrayList<>();

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "addresses")
  @XmlElement(name = "addresses")
  private List<OgcApiAddress> addresses = new ArrayList<>();

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "links")
  @XmlElement(name = "links")
  private List<OgcApiLink> links = new ArrayList<>();

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "hoursOfService")
  @XmlElement(name = "hoursOfService")
  private String hoursOfService;

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "contactInstructions")
  @XmlElement(name = "contactInstructions")
  private String contactInstructions;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @XmlElementWrapper(name = "roles")
  @XmlElement(name = "roles")
  private List<String> roles = new ArrayList<>();

  public OgcApiContact() {
  }

  /**
   * create from the Elastic Index JSON node for the contact.
   *
   * @param contactMap from Elastic Index for a single contact
   * @return parsed contact
   */
  public static OgcApiContact fromIndexMap(Map<String, Object> contactMap) {
    if (contactMap == null) {
      return null;
    }
    OgcApiContact result = new OgcApiContact();
    if (StringUtils.hasText((String) contactMap.get("role"))) {
      result.getRoles().add(contactMap.get("role").toString());
    }
    if (contactMap.get("organisationObject") != null) {
      result.setOrganization(
          CollectionInfoBuilder.getLangString(contactMap.get("organisationObject")));
    }
    if (StringUtils.hasText((String) contactMap.get("email"))) {
      result.getEmails().add(new OgcApiEmail(contactMap.get("email").toString()));
    }

    if (StringUtils.hasText((String) contactMap.get("position"))) {
      result.setPosition(contactMap.get("position").toString());
    }

    if (StringUtils.hasText((String) contactMap.get("individual"))) {
      result.setName(contactMap.get("individual").toString());
    }

    if (StringUtils.hasText((String) contactMap.get("position"))) {
      result.setPosition(contactMap.get("position").toString());
    }

    if (StringUtils.hasText((String) contactMap.get("phone"))) {
      result.getPhones().add(new OgcApiPhone(contactMap.get("phone").toString()));
    }

    if (StringUtils.hasText((String) contactMap.get("address"))) {
      result.getAddresses().add(new OgcApiAddress(contactMap.get("address").toString()));
    }

    if (StringUtils.hasText((String) contactMap.get("logo"))) {
      var logo = new OgcApiLink(contactMap.get("logo").toString(), "image/*");
      logo.setRel("icon");
      result.setLogo(logo);
    }

    if (StringUtils.hasText((String) contactMap.get("website"))) {
      var website = new OgcApiLink(contactMap.get("website").toString(), "text/html");
      result.getLinks().add(website);
    }

    return result;
  }


  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public OgcApiLink getLogo() {
    return logo;
  }

  public void setLogo(OgcApiLink logo) {
    this.logo = logo;
  }

  public List<OgcApiPhone> getPhones() {
    return phones;
  }

  public void setPhones(List<OgcApiPhone> phones) {
    this.phones = phones;
  }

  public List<OgcApiEmail> getEmails() {
    return emails;
  }

  public void setEmails(List<OgcApiEmail> emails) {
    this.emails = emails;
  }

  public List<OgcApiAddress> getAddresses() {
    return addresses;
  }

  public void setAddresses(List<OgcApiAddress> addresses) {
    this.addresses = addresses;
  }

  public List<OgcApiLink> getLinks() {
    return links;
  }

  public void setLinks(List<OgcApiLink> links) {
    this.links = links;
  }

  public String getHoursOfService() {
    return hoursOfService;
  }

  public void setHoursOfService(String hoursOfService) {
    this.hoursOfService = hoursOfService;
  }

  public String getContactInstructions() {
    return contactInstructions;
  }

  public void setContactInstructions(String contactInstructions) {
    this.contactInstructions = contactInstructions;
  }

  public List<String> getRoles() {
    return roles;
  }

  public void setRoles(List<String> roles) {
    this.roles = roles;
  }
}
