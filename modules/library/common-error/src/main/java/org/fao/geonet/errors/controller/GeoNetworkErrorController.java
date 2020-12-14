/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.errors.controller;

import java.io.StringWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.io.IOUtils;
import org.fao.geonet.errors.model.GeoNetworkError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Basic Controller which is called for unhandled Error.
 */
@Controller
public class GeoNetworkErrorController implements ErrorController {

  @Autowired
  MessageSource messages;

  /**
   * Test error.
   */
  @GetMapping(value = "/errortest")
  public String errorTest() {
    throw new IllegalArgumentException("Error reported");
  }


  /**
   * Test error localized.
   */
  @GetMapping(value = "/errortestlocalized")
  public String errorTestException(
      HttpServletRequest request) {
    throw new IllegalArgumentException(messages.getMessage(
        "api.exception.record.notFound", new String[]{"abcd"},
        request.getLocale()));
  }

  /**
   * XML or JSON error.
   */
  @RequestMapping(
      value = "/error",
      produces = {
          MediaType.APPLICATION_JSON_VALUE,
          MediaType.APPLICATION_XML_VALUE
      }
  )
  @ResponseBody
  public HttpEntity<GeoNetworkError> handleError(
      HttpServletRequest request) {
    GeoNetworkError error = getError(request);
    return ResponseEntity
        .status(error.getStatus())
        .body(error);
  }

  /**
   * HTML version of an error.
   */
  @RequestMapping(
      value = "/error",
      produces = {
          MediaType.TEXT_HTML_VALUE,
          MediaType.APPLICATION_XHTML_XML_VALUE
      }
  )
  public String handleErrorAsHtml(
      HttpServletRequest request,
      Model model) {
    StringWriter sw = new StringWriter();
    try {
      JAXBContext context = JAXBContext.newInstance(GeoNetworkError.class);
      Marshaller marshaller = context.createMarshaller();
      marshaller.marshal(getError(request), sw);
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    model.addAttribute("source", IOUtils.toInputStream(sw.toString()));
    return "core/errors/error";
  }

  protected GeoNetworkError getError(HttpServletRequest request) {
    Integer status = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    Exception exception = (Exception) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
    String errorMessage = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
    String path = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
    return new GeoNetworkError(exception, errorMessage, HttpStatus.valueOf(status), path);
  }

  @Override
  public String getErrorPath() {
    return null;
  }
}