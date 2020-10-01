/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.fao.geonet.indexing;

import java.util.Locale;
import org.fao.geonet.repository.GeonetRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;


@SpringBootApplication
@EntityScan(basePackages = {
    "org.fao.geonet.domain"
})
@EnableJpaRepositories(
    basePackages = "org.fao.geonet.repository",
    repositoryBaseClass = GeonetRepositoryImpl.class
)
@ComponentScan("org.fao.geonet")
@RefreshScope
public class GnIndexingApp {

  public static void main(String[] args) {
    SpringApplication.run(GnIndexingApp.class, args);
  }

  /**
   * Experiment localized message.
   */
  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver localeResolver = new SessionLocaleResolver();
    localeResolver.setDefaultLocale(Locale.ENGLISH);
    return localeResolver;
  }


  /**
   * Experiment localized message.
   */
  @Bean
  public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setUseCodeAsDefaultMessage(true);
    messageSource.setBasenames("messages");
    return messageSource;
  }
}
