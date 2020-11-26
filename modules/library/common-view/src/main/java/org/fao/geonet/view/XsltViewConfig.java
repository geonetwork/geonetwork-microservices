/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.view;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.saxon.s9api.ItemTypeFactory;
import net.sf.saxon.s9api.XdmMap;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.fao.geonet.standards.model.labels.Labels;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.xslt.XsltViewResolver;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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

  public static void addi18n(Model model, Locale locale) {
    addi18n(model, locale, new ArrayList<>());
  }

  /**
   * Adds language and map of translations as parameters to the model.
   */
  public static void addi18n(Model model, Locale locale, List<String> standards) {
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

    for (String standard : standards) {
      ClassPathResource labels = new ClassPathResource(String.format(
          "%s/i18n/%s/labels.xml", standard, twoLetterCode));
      if (!labels.exists()) {
        // Go to default language
      } else {
        try {
          DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
          DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
          Document doc = docBuilder.parse(labels.getFile());
          model.addAttribute("i18nStandard", doc);
        } catch (IOException ioException) {
          ioException.printStackTrace();
        } catch (ParserConfigurationException | SAXException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
