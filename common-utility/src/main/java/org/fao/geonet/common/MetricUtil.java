package org.fao.geonet.common;

public class MetricUtil {
  public static final String METRIC_PREFIX = "gn";

  public static String buildName(String group, String name) {
    return String.format("%s_%s_%s",
        METRIC_PREFIX, group, name);
  }
}
