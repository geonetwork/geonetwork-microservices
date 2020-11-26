package org.fao.geonet.common.search.domain.es;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class EsRecordSource {
  private String schema;
  private List<EsRecordContact> contactForResource;
  private List<EsRecordLink> link;
  @JsonProperty("cl_status")
  private List<EsRecordStatus> clStatus;
  private List<String> resourceType;
}