package org.fao.geonet.ogcapi.records;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.fao.geonet.common.search.GnMediaType;
import org.fao.geonet.common.search.SearchConfiguration;
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

  @Autowired
  SearchConfiguration searchConfiguration;

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer
        .favorParameter(true)
        .parameterName("f")
        .defaultContentType(MediaType.parseMediaType(searchConfiguration.getDefaultMimeType()));

    configurer.mediaType("opensearch",
        GnMediaType.APPLICATION_OPENSEARCH_XML);

    searchConfiguration.getFormats().forEach(f -> {
      configurer.mediaType(f.getName(),
          MediaType.parseMediaType(f.getMimeType()));
    });

  }


  /**
   * Resolve locale based on l URL parameter or fallback on Header.
   */
  @Bean
  public LocaleResolver localeResolver() {
    final AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver() {
      @Override
      public Locale resolveLocale(HttpServletRequest request) {
        String locale = request.getParameter("l");
        return locale != null
            ? org.springframework.util.StringUtils.parseLocaleString(locale)
            : super.resolveLocale(request);
      }
    };

    List<Locale> supportedLocales = languageRepository
        .findAll()
        .stream()
        .map(Language::getId)
        .map(Locale::forLanguageTag)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    resolver.setSupportedLocales(supportedLocales);
    resolver.setDefaultLocale(Locale.ENGLISH);
    return resolver;
  }
}
