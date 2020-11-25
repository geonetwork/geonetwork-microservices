package org.fao.geonet.ogcapi.records;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.fao.geonet.domain.Language;
import org.fao.geonet.repository.IsoLanguageRepository;
import org.fao.geonet.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class MvcConfigurer extends WebMvcConfigurerAdapter {

  @Autowired
  LanguageRepository languageRepository;

  @Autowired
  IsoLanguageRepository isoLanguageRepository;

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer
        .favorParameter(true)
        .parameterName("f")
        .defaultContentType(MediaType.APPLICATION_JSON);
  }

  /**
   * Resolve locale.
   */
  @Bean
  public LocaleResolver localeResolver() {
    final AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();

    List<Language> languageList = languageRepository.findAll();
    List<Locale> supportedLocales = new ArrayList<>();
    languageList.forEach(lang -> {
      Locale locale = Locale.forLanguageTag(lang.getId());
      if (locale != null) {
        supportedLocales.add(locale);
      }
    });

    resolver.setSupportedLocales(supportedLocales);
    resolver.setDefaultLocale(Locale.ENGLISH);
    return resolver;
  }
}
