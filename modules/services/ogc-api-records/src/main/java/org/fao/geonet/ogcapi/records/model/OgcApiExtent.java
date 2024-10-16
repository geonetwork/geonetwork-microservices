/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * https://schemas.opengis.net/ogcapi/features/part1/1.0/openapi/schemas/extent.yaml
 *
 * <p>The extent of the features in the collection. In the Core only spatial and temporal extents
 * are specified. Extensions may add additional members to represent other extents, for example,
 * thermal or pressure ranges.
 *
 * <p>An array of extents is provided for each extent type (spatial, temporal). The first item in
 * the array describes the overall extent of the data. All subsequent items describe more precise
 * extents, e.g., to identify clusters of data. Clients only interested in the overall extent will
 * only need to access the first extent in the array.
 */
public class OgcApiExtent {

  /**
   * The spatial extent of the features in the collection.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "spatial")
  @XmlElement(name = "spatial")
  public OgcApiSpatialExtent spatial;

  /**
   * The temporal extent of the features in the collection.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "temporal")
  @XmlElement(name = "temporal")
  public OgcApiTemporalExtent temporal;

  public OgcApiExtent(OgcApiSpatialExtent spatial, OgcApiTemporalExtent temporal) {
    this.spatial = spatial;
    this.temporal = temporal;
  }

  public OgcApiSpatialExtent getSpatial() {
    return spatial;
  }

  public void setSpatial(OgcApiSpatialExtent spatial) {
    this.spatial = spatial;
  }

  public OgcApiTemporalExtent getTemporal() {
    return temporal;
  }

  public void setTemporal(OgcApiTemporalExtent temporal) {
    this.temporal = temporal;
  }
}
