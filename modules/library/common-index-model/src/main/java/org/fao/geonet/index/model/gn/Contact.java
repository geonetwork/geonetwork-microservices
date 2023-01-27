/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.gn;

import static org.fao.geonet.index.model.gn.IndexRecordFieldNames.organisationName;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact {

  private String role;
  private String individual;

  @JsonProperty(organisationName)
  private Map<String, String> organisation = new HashMap<>();
  private String email;
  private String logo;
  private String phone;
  private String address;
  private String website;
  private String position;
}
