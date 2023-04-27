/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.fao.geonet.common.search.SearchConfiguration;
import org.fao.geonet.common.search.SearchConfiguration.Operations;
import org.fao.geonet.domain.Source;
import org.fao.geonet.domain.SourceType;
import org.fao.geonet.ogcapi.records.controller.model.Conformance;
import org.fao.geonet.ogcapi.records.controller.model.Content;
import org.fao.geonet.ogcapi.records.controller.model.Link;
import org.fao.geonet.ogcapi.records.controller.model.Root;
import org.fao.geonet.ogcapi.records.model.XsltModel;
import org.fao.geonet.ogcapi.records.util.CollectionInfoBuilder;
import org.fao.geonet.ogcapi.records.util.LinksItemsBuilder;
import org.fao.geonet.ogcapi.records.util.MediaTypeUtil;
import org.fao.geonet.repository.SourceRepository;
import org.fao.geonet.view.ViewUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "OGC API Records")
@Controller
public class CapabilitiesApiController {

  public static final String CONFORMANCE_REL = "conformance";
  private static final String SOURCE_ATTR = "source";

  @Value("${gn.baseurl}")
  String baseUrl;

  @Autowired
  MessageSource messages;

  @Autowired
  ViewUtility viewUtility;

  @Autowired
  @Qualifier("xsltViewResolver")
  ViewResolver viewResolver;

  @Autowired
  ConcurrentMapCacheManager cacheManager;

  @Autowired
  private SourceRepository sourceRepository;

  @Autowired
  private SearchConfiguration configuration;

  @Autowired
  MediaTypeUtil mediaTypeUtil;

  /**
   * Landing page end-point.
   *
   */
  @io.swagger.v3.oas.annotations.Operation(
      summary = "Landing page.",
      description = "The landing page provides links to start exploration of the resources "
          + "offered by an API. Its most important component is a list of links. OGC API - Common "
          + "already requires some common links. Those links are sufficient for this standard.")
  @GetMapping(value = "/",
      produces = {MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE,
          MediaType.TEXT_HTML_VALUE})
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Landing page.")
  })
  @ResponseBody
  public ResponseEntity<Root> getLandingPage(@ApiIgnore HttpServletRequest request,
      @ApiIgnore HttpServletResponse response,
      @ApiIgnore Model model) throws Exception {
    String requestBaseUrl = request.getRequestURL().toString();

    MediaType mediaType = mediaTypeUtil.calculatePriorityMediaTypeFromRequest(request);

    if (!mediaType.equals(MediaType.TEXT_HTML)) {
      Root root = new Root();

      List<Source> sources = sourceRepository.findByType(SourceType.portal, null);
      if (!sources.isEmpty()) {
        Source source = sources.get(0);
        String label = source.getLabel("eng");
        root.title(StringUtils.isEmpty(label) ? source.getName() : label);
        root.description("");
      }

      root.addLinksItem(new Link()
          .href(requestBaseUrl)
          .rel("self").type(MediaType.APPLICATION_JSON.toString()));

      configuration.getFormats(Operations.root).forEach(f -> root.addLinksItem(new Link()
          .href(requestBaseUrl + "collections?f=" + f.getName())
          .type("Catalogue collections")
          .rel("self").type(f.getMimeType())));

      addOpenApiLinks(root, requestBaseUrl);
      addConformanceLinks(root, requestBaseUrl);

      return ResponseEntity.ok(root);
    } else {
      List<Source> sources = sourceRepository.findAll();
      XsltModel modelSource = new XsltModel();
      modelSource.setOutputFormats(configuration.getFormats(Operations.collections));
      modelSource.setCollections(sources);
      model.addAttribute(SOURCE_ATTR, modelSource.toSource());
      Locale locale = LocaleContextHolder.getLocale();
      viewUtility.addi18n(model, locale, request);

      View view = viewResolver.resolveViewName("ogcapir/landingpage", locale);
      view.render(model.asMap(), request, response);

      return ResponseEntity.ok(new Root());
    }

  }

  private void addOpenApiLinks(Root root, String baseUrl) {
    String title = "The OpenAPI Documentation";
    root.addLinksItem(new Link()
        .href(baseUrl + "openapi")
        .title(title)
        .rel("service-doc").type(MediaType.TEXT_HTML_VALUE));

    root.addLinksItem(new Link()
        .href(baseUrl + "openapi?f=json")
        .title(title + " as JSON")
        .rel("service-desc").type(MediaType.APPLICATION_JSON_VALUE));
  }

  private void addConformanceLinks(Root root, String baseUrl) {
    String title = "The Conformance classes";
    root.addLinksItem(new Link()
        .href(baseUrl + CONFORMANCE_REL)
        .title(title)
        .rel(CONFORMANCE_REL).type(MediaType.TEXT_HTML_VALUE));

    root.addLinksItem(new Link()
        .href(baseUrl + CONFORMANCE_REL + "?f=json")
        .title(title + " as JSON")
        .rel(CONFORMANCE_REL).type(MediaType.APPLICATION_JSON_VALUE));
  }


  /**
   * Conformance declaration.
   */
  @io.swagger.v3.oas.annotations.Operation(
      summary = "Conformance declaration",
      description = "Conformance classes.")
  @GetMapping(value = "/conformance",
      produces = {MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE,
          MediaType.TEXT_HTML_VALUE})
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Conformance declaration.")
  })
  @ResponseBody
  public ResponseEntity<Conformance> conformanceDeclaration(@ApiIgnore HttpServletRequest request,
      @ApiIgnore HttpServletResponse response,
      @ApiIgnore Model model) throws Exception {

    Locale locale = LocaleContextHolder.getLocale();

    MediaType mediaType = mediaTypeUtil.calculatePriorityMediaTypeFromRequest(request);

    Conformance conformance = new Conformance();
    conformance.setConformsTo(List.of(
        "http://www.opengis.net/spec/ogcapi-records-1/1.0/conf/core",
        "http://www.opengis.net/spec/ogcapi-records-1/1.0/conf/searchable-catalogue",
        "http://www.opengis.net/spec/ogcapi-records-1/1.0/conf/record-collection",
        "http://www.opengis.net/spec/ogcapi-records-1/1.0/conf/records-api",
        "http://www.opengis.net/spec/ogcapi-records-1/1.0/conf/html",
        "http://www.opengis.net/spec/ogcapi-records-1/1.0/conf/json",
        "http://www.opengis.net/spec/ogcapi-records-1/1.0/conf/sorting"
        ));

    if (!mediaType.equals(MediaType.TEXT_HTML)) {
      return ResponseEntity.ok(conformance);
    } else {
      List<Source> sources = sourceRepository.findAll();
      XsltModel modelSource = new XsltModel();
      modelSource.setOutputFormats(configuration.getFormats(Operations.conformance));
      modelSource.setCollections(sources);
      modelSource.setConformance(conformance);
      model.addAttribute(SOURCE_ATTR, modelSource.toSource());
      viewUtility.addi18n(model, locale, request);
      View view = viewResolver.resolveViewName("ogcapir/conformance", locale);
      view.render(model.asMap(), request, response);

      return ResponseEntity.ok(conformance);
    }
  }


  /**
   * Collections information end-point.
   *
   */
  @io.swagger.v3.oas.annotations.Operation(
      summary = "Collections available from this API.",
      description = "Returns a metadata document that describes the collections available "
          + "from this API.")
  @GetMapping(value = "/collections",
      produces = {MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE,
          MediaType.TEXT_HTML_VALUE})
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Collections description.")
  })
  @ResponseBody
  public ResponseEntity<Content> describeCollections(@ApiIgnore HttpServletRequest request,
      @ApiIgnore HttpServletResponse response,
      @ApiIgnore Model model) throws Exception {

    Locale locale = LocaleContextHolder.getLocale();
    String language = locale.getISO3Language();

    MediaType mediaType = mediaTypeUtil.calculatePriorityMediaTypeFromRequest(request);

    if (!mediaType.equals(MediaType.TEXT_HTML)) {
      Content content = new Content();

      String requestBaseUrl = request.getRequestURL()
          .toString();

      List<Source> sources = sourceRepository.findAll();
      sources.forEach(s -> content.addCollectionsItem(
          CollectionInfoBuilder.buildFromSource(
              s, language, requestBaseUrl, configuration.getFormat(mediaType), configuration)));

      // TODO: Accept format parameter.
      List<Link> linkList = LinksItemsBuilder.build(
          configuration.getFormat(mediaType), requestBaseUrl, language, configuration);
      linkList.forEach(content::addLinksItem);

      return ResponseEntity.ok(content);
    } else {
      List<Source> sources = sourceRepository.findAll();
      XsltModel modelSource = new XsltModel();
      modelSource.setOutputFormats(configuration.getFormats(Operations.collections));
      modelSource.setCollections(sources);
      model.addAttribute(SOURCE_ATTR, modelSource.toSource());
      viewUtility.addi18n(model, locale, request);

      View view = viewResolver.resolveViewName("ogcapir/collections", locale);
      view.render(model.asMap(), request, response);

      return ResponseEntity.ok(new Content());
    }
  }
}
