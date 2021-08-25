package org.fao.geonet.dataviz.model;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DataSource {
  private URI uri;
  private Charset encoding = StandardCharsets.UTF_8;
  private AuthCredentials auth;
  
  public static DataSource fromUri(URI dataUri) {
    return new DataSource().setUri(dataUri);
  }
}
