/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.searching.controller;

import java.util.Locale;
import java.util.UUID;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.Produces;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ViewResolver;

@Controller
public class CollectionsController {

  @Autowired
  ViewResolver viewResolver;

  @Autowired
  MessageSource messages;

  /**
   * Catalogue landing page.
   */
  @GetMapping(value = "/landingpage")
  @Produces(value = "text/html")
  public String landingpage(
      @RequestParam(defaultValue = "false")
          boolean debug,
      Model model) {
    model.addAttribute("source", IOUtils.toInputStream(
        String.format("<source id='%s'/>", UUID.randomUUID())));
    model.addAttribute("language", "fre");
    return "landingpage";
  }
}
