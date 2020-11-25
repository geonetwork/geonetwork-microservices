/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.searching;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.fao.geonet.common.search.ElasticSearchProxy;
import org.fao.geonet.domain.Profile;
import org.fao.geonet.searching.controller.XsltSearchController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@EnableWebMvc
@EnableWebSecurity
@ContextConfiguration(classes = {
    XsltSearchController.class,
    SecurityConfigurer.class})
public class XsltSearchControllerSecurityConfigurerTest {

  @MockBean
  private ElasticSearchProxy esProxy;

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private FilterChainProxy springSecurityFilterChain;

  @Autowired
  private JwtAccessTokenConverter jwtAccessTokenConverter;

  @Before
  public void setup() throws Exception {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(springSecurityFilterChain).build();
  }

  private MockMvc mockMvc;

  @Test
  public void withProfileAndExtraGroupsForEveryRoles() throws Exception {
    Map gnAuthAttributes = new HashMap();
    gnAuthAttributes.put("group", 42);
    gnAuthAttributes.put("profile", Profile.Editor.name());
    Stream.of(Profile.values())
        .forEach(profile -> {gnAuthAttributes.put(profile.name(), Arrays.asList(profile.ordinal()));});
    GrantedAuthority gnAuthority = new OAuth2UserAuthority("gn", gnAuthAttributes);
    String token = createToken("42_editor", Collections.singletonList(gnAuthority));

    Mockito
        .doAnswer(invocationOnMock -> {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          assertEquals("42_editor", authentication.getName());
          OAuth2UserAuthority authority =
              (OAuth2UserAuthority) authentication.getAuthorities().stream().findFirst().get();
          assertEquals(gnAuthAttributes, authority.getAttributes());
          assertEquals("gn", authority.getAuthority());
          return null;})
        .when(esProxy).search(Mockito.any(),Mockito.any(),Mockito.any(), Mockito.any(), Mockito.any());

    this.mockMvc
        .perform(
            post("/portal/api/search/records/xslt")
                .header("Authorization", "Bearer " + token))
        .andExpect(status()
            .isOk());
  }

  @Test
  public void exoticOAuth2UserAuthorityNoSerialisationTrouble() throws Exception {
    Map attributes= new HashMap();
    attributes.put("lapin", "malin");
    Map insideMap = new HashMap();
    insideMap.put("1", "one");  // if key is an Integer, transformed into a String
    insideMap.put("2", "two");
    attributes.put("map", insideMap);
    GrantedAuthority exoticAuthority = new OAuth2UserAuthority("exotic", attributes);
    String token = createToken("gn_admin", Collections.singletonList(exoticAuthority));

    Mockito
      .doAnswer(invocationOnMock -> {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertEquals("gn_admin", authentication.getName());
        OAuth2UserAuthority authority =
            (OAuth2UserAuthority) authentication.getAuthorities().stream().findFirst().get();
        assertEquals(attributes, authority.getAttributes());
        assertEquals("exotic", authority.getAuthority());
        return null;})
      .when(esProxy).search(Mockito.any(),Mockito.any(),Mockito.any(), Mockito.any(), Mockito.any());

    this.mockMvc
      .perform(
          post("/portal/api/search/records/xslt")
          .header("Authorization", "Bearer " + token))
      .andExpect(status()
      .isOk());
  }


  private String createToken( String userName, List<GrantedAuthority> authorities) {
    UsernamePasswordAuthenticationToken token
        = new UsernamePasswordAuthenticationToken(userName, null, authorities);
    OAuth2Request request
        = new OAuth2Request(null, "clientId", null, true, null, null, null, null, null);
    OAuth2Authentication auth = new OAuth2Authentication(request, token);
    return jwtAccessTokenConverter.enhance(new DefaultOAuth2AccessToken(""), auth).getValue();
  }

}
