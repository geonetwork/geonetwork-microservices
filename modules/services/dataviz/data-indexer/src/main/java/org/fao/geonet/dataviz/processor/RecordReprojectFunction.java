/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.processor;

import java.util.function.Function;
import lombok.NonNull;
import org.fao.geonet.dataviz.model.GeodataRecord;
import org.fao.geonet.dataviz.model.GeometryProperty;
import org.locationtech.jts.geom.Geometry;

/**
 * Imperative (blocking) function that reprojects the geometry property of a
 * {@link GeodataRecord} to the coordinate reference system given at the
 * function's constructor, and returns a new {@link GeodataRecord} with the
 * reprojected geometry and the same other properties than the original record.
 *
 */
public class RecordReprojectFunction implements Function<GeodataRecord, GeodataRecord> {

  private final GeometryReprojectFunction reproject;
  private final String targetSrs;

  public RecordReprojectFunction(@NonNull String targetSrs) {
    this.targetSrs = targetSrs;
    this.reproject = new GeometryReprojectFunction(targetSrs);
  }

  @Override
  public GeodataRecord apply(GeodataRecord source) {
    if (source == null || source.getGeometry() == null) {
      return source;
    }
    GeometryProperty orig = source.getGeometry();
    Geometry reprojected = reproject(orig.getValue(), orig.getSrs());
    GeometryProperty reprojectedProp = orig.withValue(reprojected).setSrs(targetSrs);
    return source.withGeometry(reprojectedProp);
  }

  private Geometry reproject(Geometry value, String sourceSrs) {
    return reproject.apply(value, sourceSrs);
  }

}
