/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.dataviz.processor.geotools;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import lombok.NonNull;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Imperative (blocking) function that reprojects the geometry to the coordinate
 * reference system given at the function's constructor.
 * <p>
 * If no SRS is provided, the original geometry is returned as-is.
 * <p>
 * If the provided SRS code cannot be related to a valid
 * {@link CoordinateReferenceSystem}, or a transform from the source to target
 * CRS can't be created, an {@link IllegalArgumentException} is thrown.
 * <p>
 * If an error occurs during the reprojection, an {@link IllegalStateException}
 * is thrown.
 */
class GeometryReprojectFunction implements BiFunction<Geometry, String, Geometry> {

  private final CoordinateReferenceSystem targetCrs;

  private ConcurrentMap<String, CoordinateReferenceSystem> crsCache = new ConcurrentHashMap<>();
  private ConcurrentMap<String, GeometryCoordinateSequenceTransformer> transformerCache = new ConcurrentHashMap<>();

  /**
   * @param targetSrs the coordinate reference system identifier to reproject to
   * @throws IllegalArgumentException if {@code targetSrs} cannot be parsed as a
   *                                  {@link CoordinateReferenceSystem}
   */
  public GeometryReprojectFunction(@NonNull String targetSrs) {
    this(parseCrs(targetSrs));
  }

  /**
   * @param targetSrs the coordinate reference system to reproject to
   */
  public GeometryReprojectFunction(@NonNull CoordinateReferenceSystem targetCrs) {
    this.targetCrs = targetCrs;
  }

  @Override
  public Geometry apply(Geometry source, String sourceSrs) {
    if (source == null) {
      return null;
    }
    if (sourceSrs == null) {
      return source;
    }

    GeometryCoordinateSequenceTransformer transformer = transformerCache.computeIfAbsent(sourceSrs,
        this::findTransformer);
    try {
      return transformer.transform(source);
    } catch (MismatchedDimensionException | TransformException e) {
      throw new IllegalStateException(e);
    }
  }

  private GeometryCoordinateSequenceTransformer findTransformer(@NonNull String sourceSrs) {
    CoordinateReferenceSystem sourceCrs = crsCache.computeIfAbsent(sourceSrs,
        GeometryReprojectFunction::parseCrs);

    MathTransform transform = findMathTransform(sourceCrs, targetCrs);
    GeometryCoordinateSequenceTransformer transformer = new GeometryCoordinateSequenceTransformer();
    transformer.setMathTransform(transform);
    return transformer;
  }

  private MathTransform findMathTransform(CoordinateReferenceSystem sourceCrs,
      CoordinateReferenceSystem targetCrs) {
    boolean lenient = true;
    try {
      return CRS.findMathTransform(sourceCrs, targetCrs, lenient);
    } catch (FactoryException e) {
      throw new IllegalArgumentException(e);
    }
  }

  private static CoordinateReferenceSystem parseCrs(@NonNull String srs) {
    try {
      final boolean longitudeFirst = true;
      return CRS.decode(srs, longitudeFirst);
    } catch (FactoryException e) {
      throw new IllegalArgumentException(e);
    }
  }

}
