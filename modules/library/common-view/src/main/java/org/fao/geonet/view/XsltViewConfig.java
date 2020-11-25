/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.view;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import net.sf.saxon.s9api.XdmMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.xslt.XsltViewResolver;

@Component
public class XsltViewConfig {

  @Value("${gn.language.default:en}")
  String defaultLanguage;

  /**
   * XSLT view.
   */
  @Bean
  public ViewResolver xsltViewResolver() {
    XsltViewResolver viewResolver = new XsltViewResolver();
    viewResolver.setUriResolver(new XsltClasspathUriResolver());
    viewResolver.setPrefix("classpath:/xslt/");
    viewResolver.setSuffix(".xsl");
    viewResolver.setSourceKey("source");
    viewResolver.setCacheTemplates(false);
    viewResolver.setCache(false);
    return viewResolver;
  }

  /**
   * Adds language and map of translations as parameters to the model.
   */
  public static void addi18n(Model model, Locale locale) {
    String twoLetterCode = locale.getLanguage();
    model.addAttribute("language", twoLetterCode);
    model.addAttribute("language3letters", locale.getISO3Language());
    try {
      // TODO: this is maybe something to cache in memory ?
      ClassPathResource resource = new ClassPathResource(String.format(
          "messages/api_%s.properties", twoLetterCode));
      if (!resource.exists()) {
        resource = new ClassPathResource("messages/api.properties");
      }
      Properties loadedProperties = PropertiesLoaderUtils.loadProperties(resource);
      Map<String, String> properties = new HashMap<>();
      loadedProperties.forEach((key, value) -> {
        properties.put(key.toString(), value.toString());
      });
      model.addAttribute("i18n", XdmMap.makeMap(properties));
    } catch (IOException ex) {
      System.out.println(String.format(
          "IO Exception while adding translations to the model."
      ));
    }
  }
}
