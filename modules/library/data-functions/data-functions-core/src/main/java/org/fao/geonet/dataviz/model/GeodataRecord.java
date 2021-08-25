/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.model;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.fao.geonet.dataviz.model.GeodataRecordImpl.GeodataRecordImplBuilder;

public interface GeodataRecord {

  String getTypeName();

  String getId();

  GeometryProperty getGeometry();

  List<SimpleProperty<?>> getProperties();

  <V> Optional<? extends SimpleProperty<?>> getProperty(@NonNull String name);

  GeodataRecord withGeometry(GeometryProperty geom);

  GeodataRecord withProperty(SimpleProperty<?> prop);

  public static Builder builder() {
    return new Builder();
  }

  static class Builder {
    private final GeodataRecordImplBuilder impl = GeodataRecordImpl.builder()
        .properties(Collections.emptyList());

    public GeodataRecord build() {
      return impl.build();
    }

    public Builder typeName(String typeName) {
      impl.typeName(typeName);
      return this;
    }

    public Builder id(String id) {
      impl.id(id);
      return this;
    }

    public Builder geometry(GeometryProperty geometry) {
      impl.geometry(geometry);
      return this;
    }

    public Builder properties(List<SimpleProperty<?>> properties) {
      impl.properties(properties);
      return this;
    }
  }

}
