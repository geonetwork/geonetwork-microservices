/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * represents a GeoJSON polygon.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeoJsonPolygon {

  public String type = "Polygon";

  public List<List<List<Double>>> coordinates = new ArrayList<>();

  /**
   * builder to construct a polygon from a bounding box.
   * @param bbox bounding box - xmin, ymin, xmax, ymax
   * @return GeoJsonPolygon with a rectangular polygon representing the bounding box.
   */
  public static GeoJsonPolygon fromBBox(List<Double> bbox) {
    var result = new GeoJsonPolygon();
    result.addCoord(bbox.get(0), bbox.get(1)); //xmin, ymin
    result.addCoord(bbox.get(2), bbox.get(1)); //xmax, ymin
    result.addCoord(bbox.get(2), bbox.get(3)); //xmax, ymax
    result.addCoord(bbox.get(0), bbox.get(3)); //xmin, ymax
    result.addCoord(bbox.get(0), bbox.get(1)); //xmin, ymin
    return result;
  }

  /**
   * helper function to add a point to the polygon.
   * @param c1 coordinate 1 (x)
   * @param c2 coordinate 2 (y)
   */
  public void addCoord(Double c1, Double c2) {
    if (coordinates.isEmpty()) {
      coordinates.add(new ArrayList<>());
    }
    coordinates.get(0).add(Arrays.asList(c1, c2));
  }
}
