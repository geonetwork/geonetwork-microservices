/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.io.IOUtils;
import org.fao.geonet.common.xml.XmlList;
import org.fao.geonet.domain.Source;
import org.fao.geonet.ogcapi.records.rest.ogc.model.Content;
import org.fao.geonet.ogcapi.records.rest.ogc.model.Link;
import org.fao.geonet.ogcapi.records.util.CollectionInfoBuilder;
import org.fao.geonet.ogcapi.records.util.LinksItemsBuilder;
import org.fao.geonet.ogcapi.records.util.MediaTypeUtil;
import org.fao.geonet.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private SourceRepository sourceRepository;

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


  /**
   * Collections as HTML.
   */
  @RequestMapping(value = "/collections",
      produces = {"text/html"},
      method = RequestMethod.GET)
  public String describeCollectionsAsHtml(
      @RequestParam(required = false) Integer limit,
      @RequestParam(required = false) ArrayList<BigDecimal> bbox,
      @RequestParam(required = false) String time,
      Model model) {
    Locale locale = LocaleContextHolder.getLocale();
    String language = locale.getISO3Language();
    List<Source> sources = sourceRepository.findAll();
    StringWriter sw = new StringWriter();
    try {
      JAXBContext context = JAXBContext.newInstance(XmlList.class, Source.class);
      Marshaller marshaller = context.createMarshaller();
      marshaller.marshal(new XmlList(sources), sw);
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    model.addAttribute("source", IOUtils.toInputStream(sw.toString()));
    model.addAttribute("language", language);
    return "ogcapir/collections";
  }

  /**
   * Collections as XML.
   */
  @RequestMapping(value = "/collections",
      produces = {"application/xml"},
      method = RequestMethod.GET)
  public ResponseEntity<Content> describeCollectionsAsXml(
      @RequestParam(required = false) Integer limit,
      @RequestParam(required = false) ArrayList<BigDecimal> bbox,
      @RequestParam(required = false) String time) {
    return describeCollections(limit, bbox, time);
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
}
