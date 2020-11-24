/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageSourceConfig {
  //  /**
  //   * Localized message.
  //   */
  //  @Bean
  //  public LocaleResolver localeResolver() {
  //    SessionLocaleResolver localeResolver = new SessionLocaleResolver();
  //    localeResolver.setDefaultLocale(Locale.ENGLISH);
  //    return localeResolver;
  //  }

  /**
   * Localized message.
   */
  @Bean
  public ResourceBundleMessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setUseCodeAsDefaultMessage(true);
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setBasenames(
        "messages/common/exception",
        "messages/api",
        "messages/view");
    return messageSource;
  }
}
