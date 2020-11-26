package org.fao.geonet.common.search.domain.es;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class EsRecordContact {
  private String individual;
  private String organisation;
  private String logo;
  private String position;
  private String email;
  private String website;
  private String role;
}
