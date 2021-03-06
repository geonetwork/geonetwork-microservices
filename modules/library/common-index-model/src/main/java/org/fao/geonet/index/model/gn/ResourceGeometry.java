/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.gn;

import java.util.List;
import lombok.Data;

@Data
public class ResourceGeometry {

  private String type;
  private List<List<Double[]>> coordinates;
}
