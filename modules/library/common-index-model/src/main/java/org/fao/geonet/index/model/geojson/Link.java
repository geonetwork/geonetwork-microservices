package org.fao.geonet.index.model.geojson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Link {
  private String href;
  private String rel;
  private String type;
  private String hreflang;
  private String title;
}

