/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import org.fao.geonet.index.model.gn.DateRange;

/**
 * cf. https://github.com/opengeospatial/ogcapi-features/blob/master/core/openapi/schemas/extent.yaml
 *
 * <p>The temporal extent of the features in the collection.
 */
public class OgcApiTemporalExtent {

  public static final String DefaultTRS = "http://www.opengis.net/def/uom/ISO-8601/0/Gregorian";
  /**
   * One or more time intervals that describe the temporal extent of the dataset. In the Core only a
   * single time interval is supported.
   *
   * <p>Extensions may support multiple intervals. The first time interval describes the overall
   * temporal extent of the data. All subsequent time intervals describe more precise time
   * intervals, e.g., to identify clusters of data. Clients only interested in the overall temporal
   * extent will only need to access the first time interval in the array (a pair of lower and upper
   * bound instants).
   *
   * <p>---
   *
   * <p>Begin and end times of the time interval. The timestamps are in the temporal coordinate
   * reference system specified in `trs`. By default this is the Gregorian calendar.
   *
   * <p>The value `null` at start or end is supported and indicates a half-bounded interval. type:
   * array minItems: 2 maxItems: 2
   *
   * <p>format: date-time nullable: true example: - '2011-11-11T12:22:11Z' - null
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "interval")
  @XmlElement(name = "interval")
  public List<String> interval = new ArrayList<>();

  /**
   * not yet official.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "resolution")
  @XmlElement(name = "resolution")
  public String resolution;

  /**
   * Coordinate reference system of the coordinates in the temporal extent (property `interval`).
   * The default reference system is the Gregorian calendar. In the Core this is the only supported
   * temporal coordinate reference system. Extensions may support additional temporal coordinate
   * reference systems and add additional enum values.
   *
   * <p>enum: - 'http://www.opengis.net/def/uom/ISO-8601/0/Gregorian' default:
   * 'http://www.opengis.net/def/uom/ISO-8601/0/Gregorian'
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "trs")
  @XmlElement(name = "trs")
  public String trs = DefaultTRS;

  /**
   * Create OgcApiTemporalExtent from the "resourceTemporalExtentDateRange" section of the GN Index
   * record. Typically this looks like this:
   *
   * <p>"resourceTemporalExtentDateRange": [ { "gte": "2000-01-01T12:29:00.000Z", "lte":
   * "2008-01-08T12:29:00.000Z" } ],
   *
   * <p>you should call this method with the { "gte": "2000-01-01T12:29:00.000Z", "lte":
   * "2008-01-08T12:29:00.000Z" } object
   *
   * @param map from the Elastic Index Record
   * @return parsed OgcApiTemporalExtent
   */
  public static OgcApiTemporalExtent fromGnIndexRecord(DateRange map) {
    if (map == null) {
      return null;
    }

    String start = map.getGte();
    String end = map.getLte();

    var result = new OgcApiTemporalExtent();
    result.trs = DefaultTRS;
    result.interval = Arrays.asList(start, end);
    return result;
  }

  public List<String> getInterval() {
    return interval;
  }

  public void setInterval(List<String> interval) {
    this.interval = interval;
  }

  public String getResolution() {
    return resolution;
  }

  public void setResolution(String resolution) {
    this.resolution = resolution;
  }

  public String getTrs() {
    return trs;
  }

  public void setTrs(String trs) {
    this.trs = trs;
  }
}
