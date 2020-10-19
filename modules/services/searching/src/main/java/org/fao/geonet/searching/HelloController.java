/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.searching;

import java.security.Principal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  /**
   * Search.
   */
  @RequestMapping("/search")
  public String search(@AuthenticationPrincipal String name, Authentication authentication, OAuth2Authentication oAuth2Authentication, Principal principal) {
    return "Search service called. You are authenticated as " + name + ", " + oAuth2Authentication.getAuthorities().stream().findFirst().get().getAuthority();
  }

}
