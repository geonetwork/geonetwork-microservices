/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.errors.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class GeoNetworkError {
  String timestamp;

  HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

  String error;
  String message;

  @XmlElement(nillable = true)
  String stackTrace;

  @XmlElement(nillable = true)
  String path;

  @XmlElement(nillable = true)
  List<GeoNetworkError> errors = new ArrayList<>();

  public GeoNetworkError(Exception exception, HttpStatus status) {
    initialize(exception, status, "");
  }

  public GeoNetworkError(Exception exception, HttpStatus status, String path) {
    initialize(exception, status, path);
  }

  private void initialize(Exception exception, HttpStatus status, String path) {
    setTimestamp(Instant.now().toString());
    setStatus(status);
    if (exception != null) {
      setMessage(exception.getMessage());
      //    if(errorProperties.getIncludeStacktrace() != IncludeStacktrace.NEVER) {
      //      setStackTrace(
      //          Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).collect(
      //              Collectors.joining("\n")));
      //    }
    }
    setPath(path);
  }
}
