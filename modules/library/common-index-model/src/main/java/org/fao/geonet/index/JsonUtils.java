/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

  /**
   * Object mapper accepting single value to be stored as array.
   */
  public static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    return objectMapper;
  }
}
