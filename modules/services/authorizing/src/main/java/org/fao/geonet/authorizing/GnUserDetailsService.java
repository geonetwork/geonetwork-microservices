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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class GnUserDetailsService implements UserDetailsService {
  //extends AbstractUserDetailsAuthenticationProvider {

  @Autowired
  private UserRepository userRepository;

  private boolean checkUserNameOrEmail = true;

  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Override
  public UserDetails loadUserByUsername(String userNameOrEmail) throws UsernameNotFoundException {
    return retrieveUser(userNameOrEmail, null);
  }

  //  @Override
  //  protected void additionalAuthenticationChecks(
  //      UserDetails userDetails,
  //      UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
  //    User gnDetails = (User) userDetails;
  //
  //    if (authentication.getCredentials() == null) {
  //      throw new UsernameNotFoundException("Authentication failed: no credentials provided");
  //    }
  //
  //    if (authentication.getCredentials().toString().isEmpty() ||
  //        !passwordEncoder()
  //            .matches(authentication.getCredentials().toString(), gnDetails.getPassword())) {
  //      throw new UsernameNotFoundException("Authentication failed: wrong password provided");
  //    }
  //  }
  //
  //  @Override
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
    //      if (authentication != null && authentication.getCredentials() != null) {
    //      }
    }
    if (user == null) {
      throw new UsernameNotFoundException(userNameOrEmail);
    }
    return user;
  }
}