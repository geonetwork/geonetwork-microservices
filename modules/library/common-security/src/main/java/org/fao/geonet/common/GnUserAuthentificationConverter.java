package org.fao.geonet.common;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GnUserAuthentificationConverter implements UserAuthenticationConverter {

  @Override
  public Map<String, ?> convertUserAuthentication(Authentication authentication) {
    Map<String, Object> response = new LinkedHashMap();
    response.put("user_name", authentication.getName());
    if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
      response.put("authorities", convertAuthorities(authentication.getAuthorities()));
    }

    return response;
  }

  @Override
  public Authentication extractAuthentication(Map<String, ?> map) {
    if (map.containsKey("user_name")) {
      Object principal = map.get("user_name");
      Collection<? extends GrantedAuthority> authorities = this.getAuthorities(map);
      return new UsernamePasswordAuthenticationToken(principal, "N/A", authorities);
    } else {
      return null;
    }
  }

  private Map<String, ?> convertAuthorities(Collection <? extends GrantedAuthority> authorities) {
    return authorities.stream()
        .filter(auth -> auth instanceof OAuth2UserAuthority)
        .map(auth -> (OAuth2UserAuthority)auth)
        .collect(Collectors.toMap(OAuth2UserAuthority::getAuthority, OAuth2UserAuthority::getAttributes));
  }

  private Collection<? extends GrantedAuthority> getAuthorities(Map<String,?> map) {
    return ((Map<String, Object>)map.get("authorities")).values().stream()
        .map(attributes -> (Map<String, Object>)attributes)
        .map(attributes -> new OAuth2UserAuthority((String) attributes.get("groupName"), attributes))
        .collect(Collectors.toList());
  }
}
