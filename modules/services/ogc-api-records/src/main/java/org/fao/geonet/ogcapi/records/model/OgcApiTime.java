package org.fao.geonet.ogcapi.records.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * OGCAPI Time. https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/time.yaml
 *
 * <p>see 8.2.7. Temporal information (ogcapi records spec).
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OgcApiTime {

  /**
   * type: string. pattern: "^\\d{4}-\\d{2}-\\d{2}$"
   */
  @JsonInclude(Include.NON_EMPTY)
  private String date;

  /**
   * type: string. pattern: "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?Z$"
   */
  @JsonInclude(Include.NON_EMPTY)
  private String timestamp;

  /**
   * Interval. minItems: 2 maxItems: 2 items: oneOf: - type: string pattern:
   * "^\\d{4}-\\d{2}-\\d{2}$" - type: string
   * pattern: "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?Z$"
   * - type: string enum: - ".."
   */
  @JsonInclude(Include.NON_EMPTY)
  private List<String> interval;

  /**
   * Minimum time period resolvable in the dataset, as an ISO 8601 duration. example: - 'P1D'
   */
  @JsonInclude(Include.NON_EMPTY)
  private String resolution;

  /**
   * Set the interval given a set of interval datetimes (1st=start,2nd=end).
   * If the intervals are null, use "..".  See ogcapi spec for intervals.
   * @param interval list of datetime objects.  Should be exactly 2, can be nulls.
   */
  public void setInterval(List<String> interval) {
    if (interval.size() != 2) {
      return;
    }
    this.interval = new ArrayList<>();

    if (interval.get(0) != null) {
      this.interval.add(interval.get(0));
    } else {
      this.interval.add("..");
    }
    if (interval.get(1) != null) {
      this.interval.add(interval.get(1));
    } else {
      this.interval.add("..");
    }
  }
}
