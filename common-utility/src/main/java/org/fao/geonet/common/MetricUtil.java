/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.common;

public class MetricUtil {
  public static final String METRIC_PREFIX = "gn";

  public static String buildName(String group, String name) {
    return String.format("%s_%s_%s",
        METRIC_PREFIX, group, name);
  }
}
