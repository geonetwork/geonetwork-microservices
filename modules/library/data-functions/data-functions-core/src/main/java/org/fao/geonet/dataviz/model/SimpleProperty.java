/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.model;

import lombok.NonNull;
import org.fao.geonet.dataviz.model.SimplePropertyImpl.SimplePropertyImplBuilder;

public interface SimpleProperty<T> {

  @NonNull
  String getName();

  T getValue();

  SimpleProperty<T> withName(String name);

  SimpleProperty<T> withValue(T value);

  static <T> Builder<T> builder() {
    return new Builder<>();
  }

  static class Builder<T> {
    private SimplePropertyImplBuilder<T> impl = SimplePropertyImpl.builder();

    public Builder<T> name(String name) {
      impl.name(name);
      return this;
    }

    public Builder<T> value(T value) {
      impl.value(value);
      return this;
    }

    public SimpleProperty<T> build() {
      return impl.build();
    }
  }
}
