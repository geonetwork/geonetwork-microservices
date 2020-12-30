package org.fao.geonet.authorizing;

import static org.fao.geonet.authorizing.GnUserDetailsService.HIGHEST_PROFILE;
import static org.fao.geonet.authorizing.GnUserDetailsService.USER_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.fao.geonet.authorizing.GnUserDetailServiceTest.TestConfig;
import org.fao.geonet.domain.Group;
import org.fao.geonet.domain.Profile;
import org.fao.geonet.domain.User;
import org.fao.geonet.domain.UserGroup;
import org.fao.geonet.repository.GroupRepository;
import org.fao.geonet.repository.UserGroupRepository;
import org.fao.geonet.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {H2JpaConfig.class, TestConfig.class})
public class GnUserDetailServiceTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  GroupRepository groupRepository;

  @Autowired
  UserGroupRepository userGroupRepository;

  @Autowired
  GnUserDetailsService toTest;

  @Test
  public void nominal() {
    Group group = new Group();
    group.setName("csc");
    groupRepository.save(group);

    User user = new User();
    user.setUsername("csc_editor");
    user.setProfile(Profile.Editor);
    userRepository.save(user);

    UserGroup userGroup = new UserGroup();
    userGroup.setGroup(group);
    userGroup.setUser(user);
    userGroup.setProfile(Profile.Editor);
    userGroupRepository.save(userGroup);

    UserDetails userDetails = toTest.loadUserByUsername("csc_editor");

    assertEquals("csc_editor", userDetails.getUsername());

    assertEquals(1, userDetails.getAuthorities().size());
    assertTrue(userDetails.getAuthorities().stream().findFirst().get() instanceof OAuth2UserAuthority);
    OAuth2UserAuthority authority = (OAuth2UserAuthority) userDetails.getAuthorities().stream().findFirst().get();
    assertEquals("gn", authority.getAuthority());

    Map<String, Object> attributes = authority.getAttributes();
    assertEquals(6, attributes.size());
    assertEquals(Arrays.asList(group.getId()), attributes.get(Profile.Editor.name()));
    assertEquals(Collections.emptyList(), attributes.get(Profile.Reviewer.name()));
    assertEquals(Collections.emptyList(), attributes.get(Profile.UserAdmin.name()));
    assertEquals(Collections.emptyList(), attributes.get(Profile.RegisteredUser.name()));
    assertEquals(Profile.Editor.name(), attributes.get(HIGHEST_PROFILE));
    assertEquals(100, attributes.get(USER_ID));
  }

  @Configuration
  static class TestConfig {
    @Bean
    public GnUserDetailsService userDetailsService() {
      return new GnUserDetailsService();
    }
  }
}
