package org.fao.geonet.searching.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import org.fao.geonet.common.search.Constants;
import org.fao.geonet.common.search.ElasticSearchProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequestMapping(value = {
    "/{portal}/api"
})
@Tag(name = "search",
    description = "Proxy for ElasticSearch catalog search operations")
@Controller
public class XsltSearchController {
  @Autowired
  ElasticSearchProxy proxy;

  /**
   * XSLT based search endpoint.
   */
  @io.swagger.v3.oas.annotations.Operation(
      summary = "XSLT Search endpoint",
      description = "")
  @RequestMapping(value = "/search/records/xslt",
      method = {
          RequestMethod.POST
      })
  @Consumes(value = "application/json+xslt")
  @Produces(value = "application/xml")
  @ResponseStatus(value = HttpStatus.OK)
  @ResponseBody
  public void xsltSearch(
      @RequestParam(defaultValue = Constants.Selection.DEFAULT_SELECTION_METADATA_BUCKET)
          String bucket,
      @Parameter(hidden = true)
          HttpSession httpSession,
      @Parameter(hidden = true)
          HttpServletRequest request,
      @Parameter(hidden = true)
          HttpServletResponse response,
      @RequestBody(required = false)
          String body,
      @Parameter(hidden = true)
          HttpEntity<String> httpEntity) throws Exception {

    System.err.println("+++++++++++++: "
        + SecurityContextHolder.getContext().getAuthentication().getName());

    proxy.search(httpSession, request, response,
        body == null
            ? "{\"from\": 0, \"size\": 20, "
            + "\"query\": {\"query_string\": "
            + "{\"query\": \"%s +isTemplate:n\"}}}" : body, bucket);
  }
}
