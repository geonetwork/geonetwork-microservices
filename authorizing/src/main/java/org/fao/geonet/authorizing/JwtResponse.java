/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.fao.geonet.authorizing;

import java.io.Serializable;

public class JwtResponse implements Serializable {

  private static final long serialVersionUID = -8091879091924046844L;
  private final String jwttoken;

  public JwtResponse(String jwttoken) {
    this.jwttoken = jwttoken;
  }

  public String getToken() {
    return this.jwttoken;
  }
}
