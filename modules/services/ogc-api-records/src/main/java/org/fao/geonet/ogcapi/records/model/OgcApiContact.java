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
import org.fao.geonet.index.model.gn.Contact;
import org.fao.geonet.ogcapi.records.util.JsonUtils;
import org.springframework.util.StringUtils;

/**
 * https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/contact.yaml
 *
 * <p>Identification of, and means of communication with, person responsible
 * for the resource.
 */
@XmlRootElement(name = "contact")
@XmlAccessorType(XmlAccessType.FIELD)
public class OgcApiContact {

  /**
   * A value uniquely identifying a contact.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "identifier")
  @XmlElement(name = "identifier")
  private String identifier;

  /**
   * The name of the responsible person.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "name")
  @XmlElement(name = "name")
  private String name;

  /**
   * The name of the role or position of the responsible person taken from the organization's formal
   * organizational hierarchy or chart.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "position")
  @XmlElement(name = "position")
  private String position;

  /**
   * Organization/affiliation of the contact.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "organization")
  @XmlElement(name = "organization")
  private String organization;

  /**
   * Graphic identifying a contact. The link relation should be `icon` and the media type should be
   * an image media type.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "logo")
  @XmlElement(name = "logo")
  private OgcApiLink logo;

  /**
   * Telephone numbers at which contact can be made.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "phones")
  @XmlElement(name = "phones")
  private List<OgcApiPhone> phones = new ArrayList<>();

  /**
   * Email addresses at which contact can be made.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "emails")
  @XmlElement(name = "emails")
  private List<OgcApiEmail> emails = new ArrayList<>();

  /**
   * Physical location at which contact can be made.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "addresses")
  @XmlElement(name = "addresses")
  private List<OgcApiAddress> addresses = new ArrayList<>();

  /**
   * On-line information about the contact.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "links")
  @XmlElement(name = "links")
  private List<OgcApiLink> links = new ArrayList<>();

  /**
   * Time period when the contact can be contacted.
   *
   * <p>example: "Hours: Mo-Fr 10am-7pm Sa 10am-22pm Su 10am-21pm"
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "hoursOfService")
  @XmlElement(name = "hoursOfService")
  private String hoursOfService;

  /**
   * Supplemental instructions on how or when to contact the responsible party.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "contactInstructions")
  @XmlElement(name = "contactInstructions")
  private String contactInstructions;

  /**
   * The set of named duties, job functions and/or permissions associated with this contact. (e.g.
   * developer, administrator, etc.).
   */
  @JsonInclude(JsonInclude.Include.NON_NULL)
  @XmlElementWrapper(name = "roles")
  @XmlElement(name = "roles")
  private List<String> roles = new ArrayList<>();

  public OgcApiContact() {
  }


  /**
   * create from the Elastic Index JSON node for the contact.
   *
   * @param contactInfo from parsed Elastic Index for a single contact
   * @return parsed contact
   */
  public static OgcApiContact fromIndexMap(Contact contactInfo) {
    if (contactInfo == null) {
      return null;
    }
    OgcApiContact result = new OgcApiContact();
    if (StringUtils.hasText(contactInfo.getRole())) {
      result.getRoles().add(contactInfo.getRole());
    }
    if (contactInfo.getOrganisation() != null && !contactInfo.getOrganisation().isEmpty()) {
      result.setOrganization(
          JsonUtils.getLangString(contactInfo.getOrganisation()));
    }
    if (StringUtils.hasText(contactInfo.getEmail())) {
      result.getEmails().add(new OgcApiEmail(contactInfo.getEmail()));
    }

    if (StringUtils.hasText(contactInfo.getPosition())) {
      result.setPosition(contactInfo.getPosition());
    }

    if (StringUtils.hasText(contactInfo.getIndividual())) {
      result.setName(contactInfo.getIndividual());
    }

    if (StringUtils.hasText(contactInfo.getPosition())) {
      result.setPosition(contactInfo.getPosition());
    }

    if (StringUtils.hasText(contactInfo.getPhone())) {
      result.getPhones().add(new OgcApiPhone(contactInfo.getPhone()));
    }

    if (StringUtils.hasText(contactInfo.getAddress())) {
      result.getAddresses().add(new OgcApiAddress(contactInfo.getAddress()));
    }

    if (StringUtils.hasText(contactInfo.getLogo())) {
      var logo = new OgcApiLink(contactInfo.getLogo(), "image/*");
      logo.setRel("icon");
      result.setLogo(logo);
    }

    if (StringUtils.hasText(contactInfo.getWebsite())) {
      var website = new OgcApiLink(contactInfo.getWebsite(), "text/html");
      result.getLinks().add(website);
    }

    return result;
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
          JsonUtils.getLangString(contactMap.get("organisationObject")));
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
