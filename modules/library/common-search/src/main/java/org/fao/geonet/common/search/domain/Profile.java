/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.common.search.domain;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The enumeration of profiles available in geonetwork.
 *
 */
public enum Profile {
  Administrator, UserAdmin(Administrator), Reviewer(UserAdmin), Editor(Reviewer),
  RegisteredUser(Editor), Guest(RegisteredUser), Monitor(Administrator);

  public static final String PROFILES_ELEM_NAME = "profiles";
  private final Set<Profile> parents;

  private Profile(Profile... parents) {
    this.parents = new HashSet<Profile>(Arrays.asList(parents));
  }

  /**
   * A case-sensitive search for profile.
   *
   * @param profile the name of the profile to check.
   */
  public static boolean exists(String profile) {
    try {
      Profile.valueOf(profile);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  /**
   * Find the profile by name but ignore case errors.
   *
   * @param profileName The profile name.
   */
  public static Profile findProfileIgnoreCase(String profileName) {
    for (Profile actualProfile : Profile.values()) {
      if (actualProfile.name().equalsIgnoreCase(profileName)) {
        return actualProfile;
      }
    }
    return null;
  }

  /**
   * Retrieve the parent profiles.
   *
   * @return Parent profiles.
   */
  public Set<Profile> getParents() {
    HashSet<Profile> parents = new HashSet<Profile>();
    for (Profile profile : values()) {
      if (profile.parents.contains(this)) {
        parents.add(profile);
      }
    }

    return parents;
  }

  /**
   * Retrieves all the profiles.
   *
   * @return Set with all the profiles.
   */
  public Set<Profile> getAll() {
    HashSet<Profile> all = new HashSet<Profile>();
    all.add(this);
    for (Profile parent : getParents()) {
      all.addAll(parent.getAll());
    }

    return all;
  }

  /**
   * Retrieves all the profile names.
   *
   * @return Set with all the profile names.
   */
  public Set<String> getAllNames() {
    HashSet<String> names = new HashSet<String>();
    for (Profile p : getAll()) {
      names.add(p.name());
    }
    return names;
  }
}
