/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.authorizing;

import org.fao.geonet.domain.User;
import org.fao.geonet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class GnUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  private boolean checkUserNameOrEmail = true;

  @Override
  public UserDetails loadUserByUsername(String userNameOrEmail) throws UsernameNotFoundException {
    return retrieveUser(userNameOrEmail, null);
  }

  protected UserDetails retrieveUser(
      String userNameOrEmail,
      UsernamePasswordAuthenticationToken authentication)
      throws AuthenticationException {
    User user = userRepository
        .findOneByUsernameAndSecurityAuthTypeIsNullOrEmpty(userNameOrEmail);
    if (user == null && checkUserNameOrEmail) {
      user = userRepository
          .findOneByEmailAndSecurityAuthTypeIsNullOrEmpty(userNameOrEmail);
    }
    if (user != null) {
      if (authentication != null && authentication.getCredentials() != null) {
        String hash = user.getPassword();
        user.getSecurity().setPassword(hash);
      }
    }
    if (user == null) {
      throw new UsernameNotFoundException(userNameOrEmail);
    }
    return user;
  }
}