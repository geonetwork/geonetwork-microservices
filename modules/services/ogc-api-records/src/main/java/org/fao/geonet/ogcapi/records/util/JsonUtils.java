/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.util;

import java.util.Map;

public class JsonUtils {

  /**
   * todo - be language aware (send in desired language).  Move to utility class.
   *
   * @param jsonNode json node for the potentially multi-language string
   * @return "correct" language value.
   */
  public static String getLangString(Object jsonNode) {
    if (jsonNode == null) {
      return null;
    }
    if ((jsonNode instanceof String)) {
      return (String) jsonNode;
    }
    if (jsonNode instanceof Map) {
      var map = (Map<String, Object>) jsonNode;
      if (map.containsKey("default")) {
        return map.get("default").toString();
      }
      return null;
    }
    return null;
  }

  /**
   * Simple utility class to get a JSON value as a string.
   *
   * @param o json object
   * @return null or o.toString()
   */
  public static String getAsString(Object o) {
    if (o == null) {
      return null;
    }
    var result = o.toString();
    return result;
  }

  /**
   * Simple utility class to get a JSON value as a double.
   *
   * @param o json object (could be integer or double)
   * @return null or double value
   */
  public static Double getAsDouble(Object o) {
    if (o == null) {
      return null;
    }
    return ((Number) o).doubleValue();
  }
}
