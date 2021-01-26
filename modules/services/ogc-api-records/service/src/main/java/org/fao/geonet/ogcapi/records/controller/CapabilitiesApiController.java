/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.fao.geonet.common.search.SearchConfiguration;
import org.fao.geonet.domain.Source;
import org.fao.geonet.ogcapi.records.CapabilitiesApi;
import org.fao.geonet.ogcapi.records.model.XsltModel;
import org.fao.geonet.ogcapi.records.rest.ogc.model.Content;
import org.fao.geonet.ogcapi.records.rest.ogc.model.Link;
import org.fao.geonet.ogcapi.records.rest.ogc.model.Root;
import org.fao.geonet.ogcapi.records.util.CollectionInfoBuilder;
import org.fao.geonet.ogcapi.records.util.LinksItemsBuilder;
import org.fao.geonet.ogcapi.records.util.MediaTypeUtil;
import org.fao.geonet.repository.SourceRepository;
import org.fao.geonet.view.ViewUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;

@Controller
public class CapabilitiesApiController implements CapabilitiesApi {

  @Value("${gn.baseurl}")
  String baseUrl;

  @Autowired
  MessageSource messages;

  @Autowired
  ViewUtility viewUtility;

  @Autowired
  ConcurrentMapCacheManager cacheManager;

  @Autowired
  private SourceRepository sourceRepository;

  @Autowired
  private SearchConfiguration searchConfiguration;


  /**
   * Only to support sample responses from {@link CapabilitiesApi}, remove once all its methods are
   * implemented.
   */
  @Autowired
  private NativeWebRequest nativeWebRequest;

  @Override
  public Optional<NativeWebRequest> getRequest() {
    return Optional.of(nativeWebRequest);
  }

  @Override
  public ResponseEntity<Root> getLandingPage() {
    String baseUrl = ((HttpServletRequest)
        nativeWebRequest.getNativeRequest()).getRequestURL()
        .toString();

    Root root = new Root();

    root.addLinksItem(new Link()
        .href(baseUrl)
        .rel("self").type(MediaType.APPLICATION_JSON.toString()));

    searchConfiguration.getFormats().forEach(f -> {
      root.addLinksItem(new Link()
          .href(baseUrl + "collections?f=" + f.getName())
          .type("Catalogue collections")
          .rel("self").type(f.getMimeType()));

    });

    return ResponseEntity.ok(root);
  }

  @Override
  public ResponseEntity<Content> describeCollections(Integer limit, List<BigDecimal> bbox,
      String time) {

    Locale locale = LocaleContextHolder.getLocale();
    String language = locale.getISO3Language();

    MediaType mediaType = MediaTypeUtil.calculatePriorityMediaTypeFromRequest(nativeWebRequest);

    Content content = new Content();

    String baseUrl = ((HttpServletRequest) nativeWebRequest.getNativeRequest()).getRequestURL()
        .toString();

    List<Source> sources = sourceRepository.findAll();
    sources.forEach(s -> {
      content.addCollectionsItem(
          CollectionInfoBuilder.buildFromSource(s, language, baseUrl, mediaType));
    });

    // TODO: Accept format parameter.
    List<Link> linkList = LinksItemsBuilder.build(mediaType, baseUrl, language);
    linkList.forEach(l -> content.addLinksItem(l));

    return ResponseEntity.ok(content);
  }

  /**
   * Collections as XML.
   */
  @RequestMapping(value = "/collections",
      produces = {
          MediaType.APPLICATION_XML_VALUE
      },
      method = RequestMethod.GET)
  public ResponseEntity<Content> describeCollectionsAsXml(
      @RequestParam(required = false) Integer limit,
      @RequestParam(required = false) ArrayList<BigDecimal> bbox,
      @RequestParam(required = false) String time) {
    return describeCollections(limit, bbox, time);
  }

  /**
   * Collections as HTML.
   */
  @RequestMapping(value = "/collections",
      produces = {
          MediaType.TEXT_HTML_VALUE
      },
      method = RequestMethod.GET)
  public String describeCollectionsAsHtml(
      @RequestParam(required = false) Integer limit,
      @RequestParam(required = false) ArrayList<BigDecimal> bbox,
      @RequestParam(required = false) String time,
      HttpServletRequest request,
      Model model) {
    List<Source> sources = sourceRepository.findAll();
    XsltModel modelSource = new XsltModel();
    modelSource.setOutputFormats(searchConfiguration.getFormats());
    modelSource.setCollections(sources);
    model.addAttribute("source", modelSource.toSource());
    Locale locale = LocaleContextHolder.getLocale();
    viewUtility.addi18n(model, locale, request);
    return "ogcapir/collections";
  }
}
