/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.searching;

//@EnableWebSecurity
//@EnableResourceServer
//@Configuration
public class SecurityConfigurer {
  //public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
  //  @Override
  //  public void configure(HttpSecurity http) throws Exception {
  //    http
  //        .authorizeRequests(a -> a
  //            .antMatchers("/search**").permitAll()
  //            .anyRequest().authenticated()
  //        ).exceptionHandling(e -> e
  //        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
  //    );
  //  }
  //
  //
  //  public TokenStore tokenStore() {
  //    return new JwtTokenStore(accessTokenConverter());
  //  }
  //
  //  /**
  //   * Access token converter which set signing key.
  //   */
  //  @Bean
  //  public JwtAccessTokenConverter accessTokenConverter() {
  //    JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
  //    converter.setSigningKey("123");
  //    return converter;
  //  }
  //
  //  /**
  //   * A default token service using JWT token store.
  //   */
  //  @Bean
  //  public DefaultTokenServices tokenServices() {
  //    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
  //    defaultTokenServices.setTokenStore(tokenStore());
  //    defaultTokenServices.setSupportRefreshToken(true);
  //    return defaultTokenServices;
  //  }
}

