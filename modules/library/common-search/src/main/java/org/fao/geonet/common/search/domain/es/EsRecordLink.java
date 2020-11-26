package org.fao.geonet.common.search.domain.es;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class EsRecordLink {
  private String protocol;
  private String name;
  private String applicationProfile;
  private String description;
  private String url;
  private int group;
}
