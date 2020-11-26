/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.searching;

import org.fao.geonet.common.TokenStoreConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@EnableResourceServer
@Configuration
@Order(4)
@Import(TokenStoreConfig.class)
public class SecurityConfigurer extends ResourceServerConfigurerAdapter {

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.anonymous()
        .authorities("ROLE_ANONYMOUS")
        .and()
        .authorizeRequests()
        .antMatchers("/**").permitAll()
        .antMatchers("/portal/**").permitAll()
        .antMatchers("/search/secured").permitAll()
        .mvcMatchers("/**").permitAll()
        .and()
        .exceptionHandling(e -> e
            .authenticationEntryPoint(
                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        );
  }
}

