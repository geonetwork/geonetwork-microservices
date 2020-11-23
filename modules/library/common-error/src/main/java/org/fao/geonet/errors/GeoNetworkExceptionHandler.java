/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.errors;

import org.fao.geonet.errors.model.GeoNetworkError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GeoNetworkExceptionHandler extends ResponseEntityExceptionHandler {

  //  @ExceptionHandler(EntityNotFoundException.class)
  //  protected ResponseEntity<Object> handleEntityNotFound(
  //      EntityNotFoundException ex) {
  //    Error apiError = new Error(ex, NOT_FOUND);
  //    return buildResponseEntity(apiError);
  //  }

  private ResponseEntity<Object> buildResponseEntity(GeoNetworkError apiError) {
    return new ResponseEntity<>(apiError, apiError.getStatus());
  }
}
