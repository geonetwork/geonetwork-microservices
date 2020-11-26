package org.fao.geonet.authorizing;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import org.fao.geonet.domain.Profile;
import org.fao.geonet.domain.User;
import org.fao.geonet.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.oauth2.common.util.JacksonJsonParser;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@EnableWebMvc
@EnableWebSecurity
@ContextConfiguration(classes = {H2JpaConfig.class, SecurityConfigurer.class, AuthentificationTest.TestConfig.class})
public class AuthentificationTest {

  @Autowired
  private WebApplicationContext wac;

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

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
  public void nominal() throws Exception {
    User user = new User();
    user.setUsername("csc_editor");
    user.setProfile(Profile.Editor);
    user.getSecurity().setPassword(passwordEncoder.encode("pré$ident"));
    userRepository.save(user);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "password");
    params.add("scope", "any");
    params.add("username", "csc_editor");
    params.add("password", "pré$ident");

    ResultActions result
        = mockMvc.perform(post("/oauth/token")
        .params(params)
        .with(httpBasic("test-client","noonewilleverguess"))
        .accept("application/json;charset=UTF-8"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"));

    String resultString = result.andReturn().getResponse().getContentAsString();

    JacksonJsonParser jsonParser = new JacksonJsonParser();
    String accessToken = jsonParser.parseMap(resultString).get("access_token").toString();

    Map decoded = ((Map)Whitebox
        .invokeMethod(jwtAccessTokenConverter, "decode", accessToken));
    assertEquals("csc_editor", decoded.get("user_name"));
  }

  @Test
  public void wrongPasswordOrUser() throws Exception {
    User user = new User();
    user.setUsername("csc_editor_2");
    user.setProfile(Profile.Editor);
    user.getSecurity().setPassword(passwordEncoder.encode("pré$ident"));
    userRepository.save(user);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "password");
    params.add("scope", "any");
    params.add("username", "csc_editor_2");
    params.add("password", "123");

    String result
        = mockMvc.perform(post("/oauth/token")
        .params(params)
        .with(httpBasic("test-client","noonewilleverguess"))
        .accept("application/json;charset=UTF-8"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andReturn().getResponse().getContentAsString();

    assertEquals("{\"error\":\"invalid_grant\",\"error_description\":\"Bad credentials\"}", result);

    params = new LinkedMultiValueMap<>();
    params.add("grant_type", "password");
    params.add("scope", "any");
    params.add("username", "mickey");
    params.add("password", "123");

    result
        = mockMvc.perform(post("/oauth/token")
        .params(params)
        .with(httpBasic("test-client","noonewilleverguess"))
        .accept("application/json;charset=UTF-8"))
        .andExpect(status().is4xxClientError())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andReturn().getResponse().getContentAsString();

    assertEquals("{\"error\":\"invalid_grant\",\"error_description\":\"Bad credentials\"}", result);
  }

  @Configuration
  static class TestConfig {

    @Bean
    public UserDetailsService userDetailsService() {
      return new GnUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
      return new StandardPasswordEncoder("secret-hash-salt=");
    }
  }
}
