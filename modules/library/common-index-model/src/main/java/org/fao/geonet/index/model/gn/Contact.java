/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.gn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact {

  private String role;
  private String individual;
  private String organisation;
  private String email;
  private String logo;
  private String phone;
  private String address;
  private String website;
  private String position;
}
