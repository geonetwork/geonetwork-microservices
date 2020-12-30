/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.common.search;

import org.fao.geonet.common.search.domain.Profile;
import org.fao.geonet.common.search.domain.UserInfo;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class FilterBuilderTest {

  FilterBuilder filterBuilder;

  @Before
  public void setup() {
    this.filterBuilder = new FilterBuilder();
  }

  @Test
  public void testFilterType() {
    UserInfo userInfo = new UserInfo();
    String queryFilter = filterBuilder.buildQueryFilter("", userInfo);
    assertTrue(queryFilter.contains("op0:(1) AND (draft:n OR draft:e)"));
    queryFilter = filterBuilder.buildQueryFilter("metadata", userInfo);
    assertTrue(queryFilter.contains("op0:(1) AND (isTemplate:n) AND (draft:n OR draft:e)"));
    queryFilter = filterBuilder.buildQueryFilter("template", userInfo);
    assertTrue(queryFilter.contains("op0:(1) AND (isTemplate:y) AND (draft:n OR draft:e)"));
    queryFilter = filterBuilder.buildQueryFilter("subtemplate", userInfo);
    assertTrue(queryFilter.contains("op0:(1) AND (isTemplate:s) AND (draft:n OR draft:e)"));
  }

  @Test
  public void testFilterForAnonymousUser() {
    UserInfo userInfo = new UserInfo();
    String queryFilter = filterBuilder.buildQueryFilter("metadata", userInfo);
    assertTrue(queryFilter.contains("op0:(1) AND (isTemplate:n) AND (draft:n OR draft:e)"));
  }

  @Test
  public void testFilterForAdministrator() {
    UserInfo userInfo = new UserInfo();
    userInfo.setHighestProfile(Profile.Administrator.name());
    String queryFilter = filterBuilder.buildQueryFilter("metadata", userInfo);
    assertTrue(queryFilter.contains("* AND (isTemplate:n) AND (draft:n OR draft:e)"));
  }

  @Test
  public void testFilterForEditor() {
    UserInfo userInfo = new UserInfo();
    userInfo.setUserName("Dad");
    userInfo.setUserId(999);
    userInfo.setHighestProfile(Profile.Editor.name());
    userInfo.setEditingGroups(Arrays.asList(new Integer[]{10, 11, 12}));
    String queryFilter = filterBuilder.buildQueryFilter("metadata", userInfo);
    assertTrue(queryFilter.contains("(op0:(1 OR 10 OR 11 OR 12) owner:999) AND (isTemplate:n) AND (draft:n OR draft:e)"));
  }
}