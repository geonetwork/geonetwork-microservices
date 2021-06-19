/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.experimental.Accessors;

@With
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SimpleProperty<T> {
  String name;
  T value;
}
