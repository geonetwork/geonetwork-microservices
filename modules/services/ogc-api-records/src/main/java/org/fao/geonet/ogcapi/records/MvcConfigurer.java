package org.fao.geonet.ogcapi.records;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.fao.geonet.common.search.SearchConfiguration;
import org.fao.geonet.domain.Language;
import org.fao.geonet.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@Slf4j(topic = "org.fao.geonet.ogcapi.records")
public class MvcConfigurer extends WebMvcConfigurerAdapter {
  @Value("${gn.api.metadata.license.name:GNU General Public License v2.0}")
  String licenseName;

  @Value("${gn.api.metadata.license.url:https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html}")
  String licenseUrl;

  @Autowired
  LanguageRepository languageRepository;

  @Autowired
  SearchConfiguration searchConfiguration;

  @Autowired
  ServletContext servletContext;

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    String defaultMimeType = MediaType.TEXT_HTML_VALUE;
    if (StringUtils.isEmpty(searchConfiguration.getDefaultMimeType())) {
      log.warn("Default mime type in current search configuration is empty."
              + " Using the default one {} but you should check your configuration. "
              + "Maybe check that common-search/application.yml "
              + "is available in your configuration folder?",
          defaultMimeType);
    } else {
      defaultMimeType = searchConfiguration.getDefaultMimeType();
    }
    configurer
        .favorParameter(true)
        .parameterName("f")
        .defaultContentType(MediaType.parseMediaType(defaultMimeType));

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


  /**
   * Bean for springfox documentation.
   */
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(new ApiInfoBuilder()
            .title("GeoNetwork Cloud OpenAPI Documentation")
            .description("An API to create, modify, and query metadata on the Web. ")
            .version("0.1")
            .contact(new Contact(
                "GeoNetwork user mailing list",
                "https://sourceforge.net/p/geonetwork/mailman/geonetwork-users/",
                "geonetwork-users@lists.sourceforge.net")
            )
            .license(licenseName)
            .licenseUrl(licenseUrl)
            .build())
        .tags(new Tag("OGC API Records", "Endpoints for OGC API Records API"))
        .select()
        .apis(RequestHandlerSelectors.basePackage(
            "org.fao.geonet.ogcapi.records.controller"))
        .paths(documentedPaths())
        .build();
  }

  private Predicate<String> documentedPaths() {
    return regex(servletContext.getContextPath() + "/.*");
  }
}
