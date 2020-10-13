package org.fao.geonet.indexing.model;

import lombok.Data;

@Data
public class Contact {
  private String role;
  private String individual;
  private String organisation;
  private String email;
  private String phone;
  private String address;
  private String website;
  private String position;
}
