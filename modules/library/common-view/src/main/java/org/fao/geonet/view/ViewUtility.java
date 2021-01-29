/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.saxon.s9api.XdmMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

@Component
public class ViewUtility {

  @Autowired
  ViewUtility self; // For cache to work.

  @Value("${gn.baseurl}")
  String baseUrl;

  @Value("${gn.legacy.url}")
  String geonetworkUrl;

  public void addi18n(Model model, Locale locale, HttpServletRequest request) {
    addi18n(model, locale, new ArrayList<>(), request);
  }

  /**
   * Adds language and map of translations as parameters to the model.
   */
  public void addi18n(Model model, Locale locale,
      List<String> standards,
      HttpServletRequest request) {
    String twoLetterCode = locale.getLanguage();
    model.addAttribute("requestUrl",
        getUrlWithNoTrailingSlash(request.getRequestURL().toString()));
    model.addAttribute("geonetworkUrl", geonetworkUrl);
    model.addAttribute("baseUrl", baseUrl);
    model.addAttribute("language", twoLetterCode);
    model.addAttribute("language3letters", locale.getISO3Language());

    Optional<Map<String, String>> properties = self.getMessages(twoLetterCode);
    if (properties.isPresent()) {
      model.addAttribute("i18n", XdmMap.makeMap(properties.get()));
    }

    for (String standard : standards) {
      Optional<Document> doc = self.getStandardLabel(twoLetterCode, standard);
      if (doc.isPresent()) {
        model.addAttribute("i18nStandard", doc.get());
      }
    }
  }

  private String getUrlWithNoTrailingSlash(String url) {
    if (url.endsWith("/")) {
      return url.substring(0, url.length() - 1);
    } else {
      return url;
    }
  }

  /**
   * Get messages.properties.
   */
  @Cacheable("gn.cloud.messages")
  public Optional<Map<String, String>> getMessages(String twoLetterCode) {
    Map<String, String> properties = new HashMap<>();
    try {
      // TODO: this is maybe something to cache in memory ?
      ClassPathResource resource = new ClassPathResource(String.format(
          "messages/api_%s.properties", twoLetterCode));
      if (!resource.exists()) {
        resource = new ClassPathResource("messages/api.properties");
      }
      Properties loadedProperties = PropertiesLoaderUtils.loadProperties(resource);

      loadedProperties.forEach((key, value) -> {
        properties.put(key.toString(), value.toString());
      });
      return Optional.of(properties);
    } catch (IOException ex) {
      System.out.println(String.format(
          "IO Exception while adding translations to the model."
      ));
    }
    return Optional.empty();
  }

  /**
   * Get standard labels.
   */
  @Cacheable("gn.cloud.standard.labels")
  public Optional<Document> getStandardLabel(String twoLetterCode, String standard) {
    ClassPathResource labels = new ClassPathResource(String.format(
        "%s/i18n/%s/labels.xml", standard, twoLetterCode));
    if (!labels.exists()) {
      // Go to default language
      return Optional.empty();
    } else {
      try {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(labels.getInputStream());
        return Optional.of(doc);
      } catch (IOException ioException) {
        ioException.printStackTrace();
      } catch (ParserConfigurationException | SAXException e) {
        e.printStackTrace();
      }
    }
    return Optional.empty();
  }
}
