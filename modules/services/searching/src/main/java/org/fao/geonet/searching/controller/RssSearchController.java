/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.searching.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import org.fao.geonet.common.search.ElasticSearchProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
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
public class RssSearchController {

  @Autowired
  ElasticSearchProxy proxy;

  /**
   * RSS Search. https://github.com/dewitt/opensearch/blob/master/opensearch-1-1-draft-6.md#opensearch-url-template-syntax
   */
  @io.swagger.v3.oas.annotations.Operation(
      summary = "RSS Search endpoint",
      description = "Note: Templates can not be retrieved.")
  @RequestMapping(value = "/search/records/rss",
      method = {
          RequestMethod.GET
      })
  @Consumes(value = "application/rss+xml")
  @Produces(value = "application/rss+xml")
  @ResponseStatus(value = HttpStatus.OK)
  @ResponseBody
  public void rssSearch(
      @Parameter(name =
          "Replaced with the keyword or keywords "
              + "desired by the search client.")
      @RequestParam(defaultValue = "")
          String searchTerms,
      @Parameter(name =
          "Replaced with the index of the first search result "
              + "desired by the search client.")
      @RequestParam(defaultValue = "0")
          Integer startIndex,
      @Parameter(name =
          "Replaced with the number of search results per page "
              + "desired by the search client.")
      @RequestParam(defaultValue = "20")
          Integer count,
      @Parameter(hidden = true)
          HttpSession httpSession,
      @Parameter(hidden = true)
          HttpServletRequest request,
      @Parameter(hidden = true)
          HttpServletResponse response,
      @Parameter(hidden = true)
          HttpEntity<String> httpEntity) throws Exception {

    String body = buildQuery(searchTerms, startIndex, count);
    proxy.search(httpSession, request, response, body, null);
  }

  private String buildQuery(String searchTerms, Integer startIndex, Integer count) {
    return String.format("{\"from\": %d, \"size\": %d, "
            + "\"query\": {\"query_string\": "
            + "{\"query\": \"%s +isTemplate:n\"}}}",
        startIndex, count, searchTerms);
  }
}
