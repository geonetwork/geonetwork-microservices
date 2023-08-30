/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.gn;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames.LinkField;

@Data
public class Link {

  private String protocol;
  @JsonProperty(IndexRecordFieldNames.LinkField.url)
  private Map<String, String> url = new HashMap<>();

  @JsonProperty(IndexRecordFieldNames.LinkField.name)
  private Map<String, String> name = new HashMap<>();

  @JsonProperty(LinkField.description)
  private Map<String, String> description = new HashMap<>();
  private String function;
  private String applicationProfile;
  private String group;
  private String mimeType;
  private String nilReason;
}
