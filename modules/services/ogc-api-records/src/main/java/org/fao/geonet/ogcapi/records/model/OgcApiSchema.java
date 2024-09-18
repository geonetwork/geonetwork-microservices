/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/schema.yaml
 *
 * <p>A scheme related to this catalog.
 *
 * <p>The description of OpenAPI v3.0.x documents, as defined by
 * https://spec.openapis.org/oas/v3.0.3
 *
 * <p>NOTE - THIS ISN'T WELL DEFINED IN THE SPECIFICATION (OR .yaml).
 * I've done my best.
 *
 * <p>I haven't included the "x-*" pattern properties
 *
 * <p>NOTE - THE SCHEMA OBJECT ISN'T FULLY DESCRIBED IN THE SUB-PROPERTIES SINCE THEY CONTAIN
 * DIFFERENT TYPES OF PATTERN PROPERTIES
 *
 * <p>TODO - when these objects are needed (currently not).  Please go through the spec and
 * .yaml to determine what's needed and that all the objects are fully filled out.
 */
@XmlRootElement(name = "schema")
@XmlAccessorType(XmlAccessType.FIELD)
public class OgcApiSchema {

  /**
   * Undefined in the spec.  Likely the openapi version number.
   *
   * <p>pattern: ^3\.0\.\d(-.+)?$
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "openapi")
  @XmlElement(name = "openapi")
  private String openapi;


  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "info")
  @XmlElement(name = "info")
  private OgcApiInfo info;

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "externalDocs")
  @XmlElement(name = "externalDocs")
  private OgcApiExternalDocumentation externalDocs;

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "servers")
  @XmlElement(name = "servers")
  private List<OgcApiServer> servers;


  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "security")
  @XmlElement(name = "security")
  private List<OgcApiSecurityRequirement> security;

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "tags")
  @XmlElement(name = "tags")
  private List<OgcApiTag> tags;

  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "paths")
  @XmlElement(name = "paths")
  private OgcApiPath paths;


  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "components")
  @XmlElement(name = "components")
  private OgcApiComponent components;

  //---------------------------------------------------------------------


  public String getOpenapi() {
    return openapi;
  }

  public void setOpenapi(String openapi) {
    this.openapi = openapi;
  }

  public OgcApiInfo getInfo() {
    return info;
  }

  public void setInfo(OgcApiInfo info) {
    this.info = info;
  }

  public OgcApiExternalDocumentation getExternalDocs() {
    return externalDocs;
  }

  public void setExternalDocs(OgcApiExternalDocumentation externalDocs) {
    this.externalDocs = externalDocs;
  }

  public List<OgcApiServer> getServers() {
    return servers;
  }

  public void setServers(List<OgcApiServer> servers) {
    this.servers = servers;
  }

  public List<OgcApiSecurityRequirement> getSecurity() {
    return security;
  }

  public void setSecurity(List<OgcApiSecurityRequirement> security) {
    this.security = security;
  }

  public List<OgcApiTag> getTags() {
    return tags;
  }

  public void setTags(List<OgcApiTag> tags) {
    this.tags = tags;
  }

  public OgcApiPath getPaths() {
    return paths;
  }

  public void setPaths(OgcApiPath paths) {
    this.paths = paths;
  }

  public OgcApiComponent getComponents() {
    return components;
  }

  public void setComponents(OgcApiComponent components) {
    this.components = components;
  }
}
