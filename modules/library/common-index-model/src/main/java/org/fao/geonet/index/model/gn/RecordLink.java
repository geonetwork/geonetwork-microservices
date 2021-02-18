/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.gn;

import lombok.Data;

@Data
public class RecordLink {

  private String type;
  private String origin;
  private String to;
  // TODO: Multilingual
  private String title;
  private String url;
  private String associationType;
  private String initiativeType;
}
