/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.With;

@Value
@With
@Builder
public class GeodataRecordImpl implements GeodataRecord {

  private String typeName;

  private String id;

  private GeometryProperty geometry;

  private @NonNull List<SimpleProperty<?>> properties;

  @Override
  public <V> Optional<? extends SimpleProperty<?>> getProperty(@NonNull String name) {
    return properties.stream().filter(p -> p.getName().equals(name)).findFirst();
  }

  @Override
  public GeodataRecord withProperty(SimpleProperty<?> prop) {
    List<SimpleProperty<?>> props = new ArrayList<>(properties);

    final int insertionPoint = Collections.binarySearch(properties, prop,
        (p1, p2) -> p1.getName().compareTo(p2.getName()));

    if (insertionPoint >= 0) {// replace
      props.set(insertionPoint, prop);
    } else {// append
      props.add(prop);
    }

    return withProperties(props);
  }
}
