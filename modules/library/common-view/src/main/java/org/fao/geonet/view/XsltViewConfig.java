/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.view;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.xslt.XsltViewResolver;

@Component
public class XsltViewConfig {
  /**
   * XSLT view.
   */
  @Bean
  public ViewResolver xsltViewResolver() {
    XsltViewResolver viewResolver = new XsltViewResolver();
    viewResolver.setPrefix("classpath:/xslt/");
    viewResolver.setSuffix(".xsl");
    viewResolver.setSourceKey("source");
    //    viewResolver.setCacheTemplates(false);
    //    viewResolver.setCache(false);
    return viewResolver;
  }
}
