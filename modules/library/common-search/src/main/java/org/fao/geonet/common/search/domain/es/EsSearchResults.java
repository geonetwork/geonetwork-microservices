package org.fao.geonet.common.search.domain.es;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@XmlRootElement(name = "search")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSearchResults {

  private Total total;

  @JsonProperty("hits")
  private List<EsRecord> results;
}
