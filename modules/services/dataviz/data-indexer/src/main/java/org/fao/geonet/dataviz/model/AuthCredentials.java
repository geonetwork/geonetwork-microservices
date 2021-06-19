package org.fao.geonet.dataviz.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AuthCredentials {

  public enum AuthType {
    basic, bearer
  }

  private AuthType type;
  private String userName;
  private String password;
  private String token;
}
