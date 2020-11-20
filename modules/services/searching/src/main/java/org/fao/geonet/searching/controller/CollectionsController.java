/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.searching.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Produces;
import org.apache.commons.io.IOUtils;
import org.fao.geonet.common.search.Constants;
import org.fao.geonet.common.search.ElasticSearchProxy;
import org.fao.geonet.common.xml.XsltUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.xslt.XsltViewResolver;

@RequestMapping(value = {
    "/landingpage"
})
@Controller
public class CollectionsController {

  @Autowired
  ViewResolver viewResolver;

  /**
   * Catalogue landing page.
   */
  @GetMapping
  @Produces(value = "text/html")
  public String landingpage(
      @RequestParam(defaultValue = "false")
      boolean debug,
      Model model) throws Exception {
    if (debug) {
      ((ContentNegotiatingViewResolver) viewResolver)
          .getViewResolvers()
          .stream()
          .filter(v -> v instanceof XsltViewResolver)
          .map(v -> (XsltViewResolver) v)
          .forEach(v -> v.clearCache());
    }

    model.addAttribute("source",  IOUtils.toInputStream(
        String.format("<source id='%s'/>", UUID.randomUUID())));
    model.addAttribute("language", "fre");
    return "landingpage";
  }
}
