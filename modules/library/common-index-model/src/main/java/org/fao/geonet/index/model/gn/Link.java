/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.gn;

import lombok.Data;
import javax.xml.bind.annotation.XmlType;

@Data
@XmlType(name = "ResourceLink")
public class Link {

  private String protocol;
  private String url;
  // TODO: Multilingual
  private String name;
  private String description;
  private String function;
  private String applicationProfile;
  private String group;
}
