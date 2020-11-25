package org.fao.geonet.ogcapi.records;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import org.fao.geonet.domain.Language;
import org.fao.geonet.repository.IsoLanguageRepository;
import org.fao.geonet.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class MvcConfigurer {

  @Autowired
  LanguageRepository languageRepository;

  @Autowired
  IsoLanguageRepository isoLanguageRepository;

  /**
   * Resolve locale.
   */
  @Bean
  public LocaleResolver localeResolver() {
    final AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();

    List<Locale> supportedLocales = languageRepository
        .findAll() //
        .stream() //
        .map(Language::getId) //
        .map(Locale::forLanguageTag) //
        .filter(Objects::nonNull) //
        .collect(Collectors.toList());

    resolver.setSupportedLocales(supportedLocales);
    resolver.setDefaultLocale(Locale.ENGLISH);
    return resolver;
  }
}
