/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.authorizing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class SecurityConfigurer extends AuthorizationServerConfigurerAdapter {

  AuthenticationManager authenticationManager;
  PasswordEncoder passwordEncoder;
  UserDetailsService userDetailsService;

  /**
   * Configure security.
   */
  public SecurityConfigurer(AuthenticationConfiguration authenticationConfiguration,
      PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) throws Exception {
    this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
    this.passwordEncoder = passwordEncoder;
    this.userDetailsService = userDetailsService;
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security
        .tokenKeyAccess("permitAll()")
        .checkTokenAccess("isAuthenticated()")
        .allowFormAuthenticationForClients();
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
        .inMemory()
        .withClient("test-client")
        .secret(passwordEncoder.encode("noonewilleverguess"))
        .scopes("any")
        .autoApprove(true)
        .authorizedGrantTypes("password", "refresh_token");
  }

  /**
   * Configure endpoints.
   */
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    endpoints.authenticationManager(this.authenticationManager)
        .accessTokenConverter(accessTokenConverter())
        .userDetailsService(userDetailsService)
        .tokenStore(tokenStore());
  }

  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  /**
   * Access token converter which set signing key.
   */
  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setSigningKey("123");
    return converter;
  }


  /**
   * A default token service using JWT token store.
   */
  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {
    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
    defaultTokenServices.setTokenStore(tokenStore());
    defaultTokenServices.setSupportRefreshToken(true);
    return defaultTokenServices;
  }
}
