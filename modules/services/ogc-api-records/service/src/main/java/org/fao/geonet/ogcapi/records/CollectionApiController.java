package org.fao.geonet.ogcapi.records;

import io.swagger.annotations.ApiParam;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.io.IOUtils;
import org.fao.geonet.common.xml.XmlList;
import org.fao.geonet.domain.Source;
import org.fao.geonet.ogcapi.records.rest.ogc.model.CollectionInfo;
import org.fao.geonet.ogcapi.records.service.CollectionService;
import org.fao.geonet.ogcapi.records.util.CollectionInfoBuilder;
import org.fao.geonet.ogcapi.records.util.MediaTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;


@Controller
public class CollectionApiController implements CollectionApi {

  @Autowired
  private NativeWebRequest nativeWebRequest;

  @Autowired
  private CollectionService collectionService;

  /**
   * Describe a collection.
   */
  @Override
  public ResponseEntity<CollectionInfo> describeCollection(
      @ApiParam(value = "Identifier (name) of a specific collection", required = true)
      @PathVariable("collectionId") String collectionId) {

    Locale locale = LocaleContextHolder.getLocale();
    String language = locale.getISO3Language();

    MediaType mediaType = MediaTypeUtil.calculatePriorityMediaTypeFromRequest(nativeWebRequest);

    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find collection");
    }

    String baseUrl = ((HttpServletRequest) nativeWebRequest.getNativeRequest()).getRequestURL()
        .toString().replace(collectionId, "");

    CollectionInfo collectionInfo = CollectionInfoBuilder
        .buildFromSource(source, language, baseUrl, mediaType);

    return ResponseEntity.ok(collectionInfo);
  }

  /**
   * Collections as XML.
   */
  @GetMapping(value = "/collections/{collectionId}",
      produces = {"text/xml"})
  public ResponseEntity<CollectionInfo> describeCollectionAsXml(
      @PathVariable("collectionId") String collectionId) {
    return describeCollection(collectionId);
  }

  /**
   * Collection as HTML.
   */
  @GetMapping(value = "/collections/{collectionId}",
      produces = {"text/html"})
  public String describeCollectionAsHtml(
      @PathVariable("collectionId") String collectionId,
      Model model) {
    Locale locale = LocaleContextHolder.getLocale();
    String language = locale.getISO3Language();
    Source source = collectionService.retrieveSourceForCollection(collectionId);
    StringWriter sw = new StringWriter();
    try {
      JAXBContext context = JAXBContext.newInstance(XmlList.class, Source.class);
      Marshaller marshaller = context.createMarshaller();
      marshaller.marshal(new XmlList<>(Arrays.asList(source)), sw);
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    model.addAttribute("source", IOUtils.toInputStream(sw.toString()));
    model.addAttribute("language", language);
    return "ogcapir/collection";
  }


  @Override
  public ResponseEntity<List<Object>> getCoverageOffering(String collectionId) {
    Source source = collectionService.retrieveSourceForCollection(collectionId);

    if (source == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find collection");
    }

    return ResponseEntity.ok(collectionService.getSortables(source));
  }
}
