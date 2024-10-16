/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.controller.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Coordinate reference system of the coordinates in the spatial extent (property `spatial`).
 * In the Core, only WGS84 longitude/latitude is supported. Extensions may support additional
 * coordinate reference systems.
 */
public enum CrsEnum {
  HTTP_WWW_OPENGIS_NET_DEF_CRS_OGC_1_3_CRS84("http://www.opengis.net/def/crs/OGC/1.3/CRS84");

  private String value;

  CrsEnum(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  /**
   * Get a CrsEnum value from a string representation.
   */
  @JsonCreator
  public static CrsEnum fromValue(String value) {
    for (CrsEnum b : CrsEnum.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}