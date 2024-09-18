/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.model;

import static org.fao.geonet.ogcapi.records.util.JsonUtils.getAsDouble;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;


/**
 * cf. https://github.com/opengeospatial/ogcapi-features/blob/master/core/openapi/schemas/extent.yaml
 *
 * <p>The spatial extent of the features in the collection.
 */
public class OgcApiSpatialExtent {

  public static final String CRS84 = "http://www.opengis.net/def/crs/OGC/1.3/CRS84";
  public static final String CRS84h = "http://www.opengis.net/def/crs/OGC/0/CRS84h";

  /**
   * One or more bounding boxes that describe the spatial extent of the dataset. In the Core only a
   * single bounding box is supported.
   *
   * <p>Extensions may support additional areas. The first bounding box describes the overall
   * spatial extent of the data. All subsequent bounding boxes describe more precise bounding boxes,
   * e.g.,to identify clusters of data. Clients only interested in the overall spatial extent will
   * only need to access the first bounding box in the array.
   *
   * <p>----
   *
   * <p>Each bounding box is provided as four or six numbers, depending on whether the coordinate
   * reference system includes a vertical axis (height or depth):
   *
   * <p>Lower left corner, coordinate axis 1 Lower left corner, coordinate axis 2 Minimum value,
   * coordinate axis 3 (optional) Upper right corner, coordinate axis 1 Upper right corner,
   * coordinate axis 2 Maximum value, coordinate axis 3 (optional)
   *
   * <p>If the value consists of four numbers, the coordinate reference system is WGS 84
   * longitude/latitude (http://www.opengis.net/def/crs/OGC/1.3/CRS84) unless a different coordinate
   * reference system is specified in `crs`.
   *
   * <p>If the value consists of six numbers, the coordinate reference system is WGS 84
   * longitude/latitude/ellipsoidal height (http://www.opengis.net/def/crs/OGC/0/CRS84h) unless a
   * different coordinate reference system is specified in `crs`.
   *
   * <p>For WGS 84 longitude/latitude the values are in most cases the sequence of minimum
   * longitude, minimum latitude, maximum longitude and maximum latitude. However, in cases where
   * the box spans the antimeridian the first value (west-most box edge) is larger than the third
   * value (east-most box edge).
   *
   * <p>If the vertical axis is included, the third and the sixth number are the bottom and the
   * top of the 3-dimensional bounding box.
   *
   * <p>If a feature has multiple spatial geometry properties, it is the decision of the server
   * whether only a single spatial geometry property is used to determine the extent or all relevant
   * geometries.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "bbox")
  @XmlElement(name = "bbox")
  public List<Double> bbox = new ArrayList<>();
  /**
   * Coordinate reference system of the coordinates in the spatial extent (property `bbox`). The
   * default reference system is WGS 84 longitude/latitude. In the Core the only other supported
   * coordinate reference system is WGS 84 longitude/latitude/ellipsoidal height for coordinates
   * with height. Extensions may support additional coordinate reference systems and add additional
   * enum values.
   *
   * <p>enum: - 'http://www.opengis.net/def/crs/OGC/1.3/CRS84' - 'http://www.opengis.net/def/crs/OGC/0/CRS84h'
   * default: 'http://www.opengis.net/def/crs/OGC/1.3/CRS84'
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "crs")
  @XmlElement(name = "crs")
  public String crs = CRS84;

  /**
   * Create OgcApiSpatialExtent from the "geom" section of the GN Index record. Typically this is a
   * polygon:
   *
   * <p>"geom":[{"type":"Polygon","coordinates":
   * [[[37.0,-3.0],[156.0,-3.0],[156.0,83.0],[37.0,83.0],[37.0,-3.0]]]}]
   *
   * <p>you should call this method with the {"type":"Polygon",
   * "coordinates":[[[37.0,-3.0],[156.0,-3.0],[156.0,83.0],[37.0,83.0],[37.0,-3.0]]]} object
   *
   * @param map from the GN Index Record JSON
   * @return parsed OgcApiSpatialExtent
   */
  public static OgcApiSpatialExtent fromGnIndexRecord(Map<String, Object> map) {
    if (map == null
        || !map.containsKey("type")
        || !map.get("type").equals("Polygon")
        || !map.containsKey("coordinates")
        || !(map.get("coordinates") instanceof List)) {
      return null;
    }
    var coords = (List) ((List) map.get("coordinates")).get(0);

    double xmin = Double.MAX_VALUE;
    double ymin = Double.MAX_VALUE;
    double xmax = Double.MIN_VALUE;
    double ymax = Double.MIN_VALUE;

    for (var myCoord : coords) {
      var coord = (List) myCoord;
      var x = getAsDouble(coord.get(0));
      var y = getAsDouble(coord.get(1));
      if (x < xmin) {
        xmin = x;
      }
      if (y < ymin) {
        ymin = y;
      }
      if (x > xmax) {
        xmax = x;
      }
      if (y > ymax) {
        ymax = y;
      }
    }

    var result = new OgcApiSpatialExtent();
    result.crs = CRS84;
    result.bbox = Arrays.asList(xmin, ymin, xmax, ymax);
    return result;
  }


  public List<Double> getBbox() {
    return bbox;
  }

  public void setBbox(List<Double> bbox) {
    this.bbox = bbox;
  }

  public String getCrs() {
    return crs;
  }

  public void setCrs(String crs) {
    this.crs = crs;
  }
}
