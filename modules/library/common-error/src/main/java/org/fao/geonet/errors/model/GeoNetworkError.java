/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.errors.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
  @JsonInclude(JsonInclude.Include.NON_NULL)
  String stackTrace;

  @XmlElement(nillable = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  String path;

  @XmlElement(nillable = true)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  List<GeoNetworkError> errors = new ArrayList<>();

  public GeoNetworkError(Exception exception, HttpStatus status) {
    initialize(exception, "", status, "");
  }


  public GeoNetworkError(Exception exception, String errorMessage,
      HttpStatus status, String path) {
    initialize(exception, errorMessage, status, path);
  }

  private void initialize(Exception exception, String errorMessage,
      HttpStatus status, String path) {
    setTimestamp(Instant.now().toString());
    setStatus(status);
    setError(status.getReasonPhrase());
    if (exception != null) {
      setMessage(exception.getMessage());
      //    if(errorProperties.getIncludeStacktrace() != IncludeStacktrace.NEVER) {
      setStackTrace(
          Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).collect(
              Collectors.joining("\n")));
      //    }
    } else {
      setMessage(errorMessage);
    }
    setPath(path);
  }
}
