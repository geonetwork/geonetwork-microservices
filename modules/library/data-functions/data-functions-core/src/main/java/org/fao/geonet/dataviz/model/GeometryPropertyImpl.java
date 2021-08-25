/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;
import org.locationtech.jts.geom.Geometry;

@Value
@With
@Builder
public class GeometryPropertyImpl implements GeometryProperty {

  private @NonNull String name;
  private Geometry value;
  private String srs;
}
