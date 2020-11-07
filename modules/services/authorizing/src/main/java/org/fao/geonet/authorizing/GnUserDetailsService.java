/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.authorizing;

import static org.springframework.data.jpa.domain.Specification.where;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.fao.geonet.domain.User;
import org.fao.geonet.domain.UserGroup;
import org.fao.geonet.repository.GroupRepository;
import org.fao.geonet.repository.UserGroupRepository;
import org.fao.geonet.repository.UserRepository;
import org.fao.geonet.repository.specification.UserGroupSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

public class GnUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserGroupRepository userGroupRepository;

  @Autowired
  private GroupRepository groupRepository;

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

    Specification<UserGroup> thisUser = where(UserGroupSpecs.hasUserId(user.getId()));
    List<UserGroup> userGroups = userGroupRepository.findAll(thisUser);

    List<GrantedAuthority> authorities = userGroups.stream()
        .map(userGroup -> {
          Map<String, Object> reservedOperation = new HashMap<>();
          reservedOperation.put("groupId", userGroup.getGroup().getId());
          reservedOperation.put("groupName", userGroup.getGroup().getName());
          reservedOperation.put("profiles", userGroup.getProfile().getAll().stream()
              .map(profile -> profile.name()).collect(Collectors.toList()));
          return new OAuth2UserAuthority(userGroup.getGroup().getName(), reservedOperation);
          })
        .collect(Collectors.toList());

    return org.springframework.security.core.userdetails.User
        .withUsername(user.getUsername())
        .password(user.getPassword())
        .roles(user.getProfile().name())
        .authorities(authorities)
        .build();
  }
}