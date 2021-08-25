/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.model;

import org.fao.geonet.dataviz.model.GeometryPropertyImpl.GeometryPropertyImplBuilder;
import org.locationtech.jts.geom.Geometry;

public interface GeometryProperty extends SimpleProperty<Geometry> {
  
  String getSrs();

  @Override
  GeometryProperty withValue(Geometry value);

  GeometryProperty withSrs(String srs);

  static Builder builder() {
    return new Builder();
  }

  static class Builder {
    private final GeometryPropertyImplBuilder impl = GeometryPropertyImpl.builder();

    public Builder name(String name) {
      impl.name(name);
      return this;
    }

    public Builder srs(String srs) {
      impl.srs(srs);
      return this;
    }

    public Builder value(Geometry value) {
      impl.value(value);
      return this;
    }

    public GeometryProperty build() {
      return impl.build();
    }
  }
}
