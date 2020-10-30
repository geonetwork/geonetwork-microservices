/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.common.search.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

public class UserInfo {
  @Getter
  @Setter
  private Integer userId;

  @Getter
  @Setter
  private String userName;

  @Getter
  @Setter
  private List<Integer> viewingGroups = new ArrayList<>();

  @Getter
  @Setter
  private List<Integer> editingGroups = new ArrayList<>();

  /**
   * Retrieves if the user is authenticated.
   *
   * @return Boolean indicating if the user is authenticated.
   */
  public boolean isAuthenticated() {
    return (StringUtils.isNotEmpty(userName)
        && (!userName.equalsIgnoreCase("anonymousUser")));
  }

  /**
   * Retrieve the user profile.
   *
   * @return User profile.
   */
  public String getProfile() {
    return "";
  }

  /**
   * Retrieve the user groups.
   *
   * @return List of user group identfiers.
   */
  public Set<Integer> getGroups() {
    Set<Integer> groupsSet = new HashSet<>();
    groupsSet.add(ReservedGroup.all.getId());

    if (isAuthenticated()) {
      groupsSet.addAll(viewingGroups);
      groupsSet.addAll(new HashSet<>(editingGroups));
    }

    return groupsSet;
  }
}
