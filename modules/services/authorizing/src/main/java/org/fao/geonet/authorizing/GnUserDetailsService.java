/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.authorizing;

import static org.springframework.data.jpa.domain.Specification.where;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.fao.geonet.domain.Profile;
import org.fao.geonet.domain.User;
import org.fao.geonet.domain.UserGroup;
import org.fao.geonet.repository.GroupRepository;
import org.fao.geonet.repository.UserGroupRepository;
import org.fao.geonet.repository.UserRepository;
import org.fao.geonet.repository.specification.UserGroupSpecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
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
    User user = userRepository.findOneByUsernameAndSecurityAuthTypeIsNullOrEmpty(userNameOrEmail);
    if (user == null && checkUserNameOrEmail) {
      user = userRepository
          .findOneByEmailAndSecurityAuthTypeIsNullOrEmpty(userNameOrEmail);
    }
    if (user == null) {
      throw new UsernameNotFoundException(userNameOrEmail);
    }
    Specification<UserGroup> thisUser = where(UserGroupSpecs.hasUserId(user.getId()));
    List<UserGroup> userGroups = userGroupRepository.findAll(thisUser);

    Map<String, List<Integer>> attributesToCast = userGroups.stream()
        .map(userGroup -> new SimpleEntry<>(
            userGroup.getProfile().name(),
            userGroup.getGroup().getId()))
        .collect(Collectors.groupingBy(
            t -> t.getKey(),
            Collectors.mapping(t -> t.getValue(), Collectors.toList())
        ));
    Map<String, Object> attributes = (Map<String, Object>) (Object) attributesToCast;
    attributes.put("highest_profile", user.getProfile().name());
    attributes.putIfAbsent(Profile.UserAdmin.name(), Collections.emptyList());
    attributes.putIfAbsent(Profile.Reviewer.name(), Collections.emptyList());
    attributes.putIfAbsent(Profile.RegisteredUser.name(), Collections.emptyList());
    attributes.putIfAbsent(Profile.Editor.name(), Collections.emptyList());
    OAuth2UserAuthority authority = new OAuth2UserAuthority("gn", attributes);

    return org.springframework.security.core.userdetails.User
        .withUsername(user.getUsername())
        .password(user.getPassword())
        .authorities(Collections.singletonList(authority))
        .build();
  }
}