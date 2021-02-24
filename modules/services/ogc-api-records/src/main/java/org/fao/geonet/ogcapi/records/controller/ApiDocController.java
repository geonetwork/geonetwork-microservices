package org.fao.geonet.ogcapi.records.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.fao.geonet.ogcapi.records.util.MediaTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;


@ApiIgnore
@Controller
public class ApiDocController {
  @Value("${springfox.documentation.openApi.v3.path}")
  String openApiDocPath;

  @Value("${springfox.documentation.swaggerUi.baseUrl}")
  String swaggerBaseUrl;

  @Autowired
  ServletContext servletContext;

  @Autowired
  MediaTypeUtil mediaTypeUtil;

  /**
   * ApiDocs end-point.
   *
   */
  @GetMapping(value = "/openapi", produces = {MediaType.APPLICATION_JSON_VALUE,
      MediaType.TEXT_HTML_VALUE})
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity openapi(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    List<MediaType> allowedMediaTypes =
        Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML);

    MediaType mediaType =
        mediaTypeUtil.calculatePriorityMediaTypeFromRequest(request, allowedMediaTypes);

    if (mediaType.equals(MediaType.APPLICATION_JSON)) {
      RestTemplate restTemplate = new RestTemplate();
      String url = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
          + servletContext.getContextPath() + openApiDocPath;
      String openApiJsonDoc = restTemplate.getForObject(url, String.class);

      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().write(openApiJsonDoc);
      response.getWriter().flush();
    } else {
      response.sendRedirect(swaggerBaseUrl + "/swagger-ui/");
    }

    return null;
  }

}
