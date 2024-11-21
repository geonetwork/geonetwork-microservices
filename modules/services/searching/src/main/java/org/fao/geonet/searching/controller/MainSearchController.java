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
import javax.ws.rs.Produces;
import org.fao.geonet.common.search.Constants;
import org.fao.geonet.common.search.ElasticSearchProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
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
public class MainSearchController {

  @Autowired
  ElasticSearchProxy proxy;

  @io.swagger.v3.oas.annotations.Operation(
      summary = "Search endpoint",
      description = "See https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html for search parameters details.")
  @RequestMapping(value = "/search/records/_search",
      method = {
          RequestMethod.POST
      })
  @Produces(value = "application/json")
  @ResponseStatus(value = HttpStatus.OK)
  @ResponseBody
  public void search(
      @RequestParam(defaultValue = Constants.Selection.DEFAULT_SELECTION_METADATA_BUCKET)
          String bucket,
      @Parameter(hidden = true)
          HttpSession httpSession,
      @Parameter(hidden = true)
          HttpServletRequest request,
      @Parameter(hidden = true)
          HttpServletResponse response,
      @RequestBody
          String body,
      @Parameter(hidden = true)
          HttpEntity<String> httpEntity) throws Exception {
    proxy.search(httpSession, request, response, body, bucket);
  }

  /**
   * provides an API endpoint for a GeoRSS endpoint, also sorts the records
   * by changeDate (descending), and limits the results to the first 10.
   *
   * @param bucket the bucket where to search
   * @param httpSession the http session object
   * @param request the HTTTP request object
   * @param response the HTTP response object
   * @param httpEntity the http entity object
   * @throws Exception the exception being thrown.
   */
  @RequestMapping(value = "/search/records/rss.search",
      method = {
          RequestMethod.GET
      })
  @Produces(value = "application/xml")
  @ResponseStatus(value = HttpStatus.OK)
  @ResponseBody
  public void rssSearch(
          @RequestParam(defaultValue = Constants.Selection.DEFAULT_SELECTION_METADATA_BUCKET)
          String bucket,
          HttpSession httpSession,
          HttpServletRequest request,
          HttpServletResponse response,
          HttpEntity<String> httpEntity) throws Exception {
    String body;
    body = "{\"from\": 0, \"size\": 10,\"query\": {\"query_string\": {\"query\": \"+isTemplate:n\"}"
        + "}, \"sort\": [{ \"changeDate\": {\"order\": \"desc\", \"unmapped_type\" : \"date\" }}]}";
    proxy.search(httpSession, request, response, body, bucket);
  }


}
