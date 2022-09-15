package org.fao.geonet.index.model.geojson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.util.List;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Record {
  private String id;
  private String type;
  private Geometry geometry;
  @JsonProperty(value = "links")
  private List<Link> links;
  @JsonProperty(value = "properties")
  private Map<String, Object> properties;
  @JsonProperty(value = "keywords")
  private List<String> keywords;
}
