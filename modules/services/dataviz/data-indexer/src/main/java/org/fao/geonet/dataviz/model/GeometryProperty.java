/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.locationtech.jts.geom.Geometry;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class GeometryProperty extends SimpleProperty<Geometry> {

  private String srs;

  public GeometryProperty(String name, Geometry value, String srs) {
    super(name, value);
    this.srs = srs;
  }

  public @Override GeometryProperty setName(String name) {
    return (GeometryProperty) super.setName(name);
  }

  public @Override GeometryProperty setValue(Geometry value) {
    return (GeometryProperty) super.setValue(value);
  }

  public @Override GeometryProperty withName(String name) {
    return new GeometryProperty(name, super.getValue(), getSrs());
  }

  public @Override GeometryProperty withValue(Geometry value) {
    return new GeometryProperty(super.getName(), value, getSrs());
  }

  public GeometryProperty withSrs(String srs) {
    return new GeometryProperty(super.getName(), super.getValue(), srs);
  }
}
