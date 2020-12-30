/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.searching.controller;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityTesterController {

  /**
   * A simple secured endpoint returning user details stored in JWT.
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
    if (authority.isPresent() && authority.get().getAuthority().equals("gn")) {
      OAuth2UserAuthority oauthAuthority = (OAuth2UserAuthority) authority.get();
      Map<String, Object> attributes = oauthAuthority.getAttributes();
      StringBuilder message = new StringBuilder();
      message.append(String.format("You are authenticated as %s\n", name));
      message.append(String.format("Authorities %s\n", oauthAuthority.getAuthority()));
      attributes.forEach((k, v) -> {
        message.append(" * ").append(k).append(":").append(v).append("\n");
      });
      return message.toString();
    } else {
      return String.format("No authority found. User is %s", name);
    }
  }
}
