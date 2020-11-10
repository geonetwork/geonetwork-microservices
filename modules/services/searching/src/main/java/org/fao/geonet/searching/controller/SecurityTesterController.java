/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.searching.controller;

import java.security.Principal;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityTesterController {

  /**
   * A simple secured endpoint returning username.
   */
  @RequestMapping("/search/secured")
  public String search(
      @AuthenticationPrincipal
          String name,
      Authentication authentication,
      OAuth2Authentication oauth2Authentication,
      Principal principal) {
    Optional<GrantedAuthority> authority =
        oauth2Authentication.getAuthorities()
            .stream().findFirst();
    return String.format(
        "Search service called. You are authenticated as %s. Authorities: %s",
        name,
        authority.isPresent() ? authority.get().getAuthority() : "");
  }
}
