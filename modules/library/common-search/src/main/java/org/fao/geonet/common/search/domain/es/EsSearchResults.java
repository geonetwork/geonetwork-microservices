package org.fao.geonet.common.search.domain.es;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSearchResults {
  private Total total;

  private List<EsRecord> results;

  public Total getTotal() {
    return total;
  }

  public void setTotal(Total total) {
    this.total = total;
  }

  @JsonProperty("hits")
  public List<EsRecord> getResults() {
    return results;
  }

  public void setResults(List<EsRecord> results) {
    this.results = results;
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public class Total {
    @JsonProperty("value")
    private int total;

    public int getTotal() {
      return total;
    }

    public void setTotal(int total) {
      this.total = total;
    }
  }
}
