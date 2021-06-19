package org.fao.geonet.dataviz.producer.geotools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.dataviz.model.GeodataRecord;
import org.fao.geonet.dataviz.model.GeometryProperty;
import org.fao.geonet.dataviz.model.SimpleProperty;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

@Slf4j
public class FeatureToRecord implements Function<SimpleFeature, GeodataRecord> {

  private Map<CoordinateReferenceSystem, String> crsToSrs = new IdentityHashMap<>();

  @Override
  public GeodataRecord apply(SimpleFeature f) {
    log.trace("Mapping feature {} to GeodataRecord", f.getID());
    GeodataRecord record = new GeodataRecord();
    record.setGeometry(getGeometryProperty(f));
    record.setProperties(getSimpleProperties(f));
    return record;
  }

  private List<SimpleProperty<?>> getSimpleProperties(SimpleFeature f) {
    Collection<Property> atts = f.getProperties();
    List<SimpleProperty<?>> props = new ArrayList<>(atts.size());

    for (Property att : atts) {
      if (!(att instanceof GeometryAttribute)) {
        SimpleProperty<?> p = new SimpleProperty<Object>(att.getName().getLocalPart(),
            att.getValue());
        props.add(p);
      }
    }
    return props;
  }

  private GeometryProperty getGeometryProperty(SimpleFeature f) {
    GeometryAttribute defaultGeometry = f.getDefaultGeometryProperty();
    if (null == defaultGeometry) {
      return null;
    }
    String name = defaultGeometry.getName().getLocalPart();
    Geometry value = (Geometry) defaultGeometry.getValue();
    CoordinateReferenceSystem crs = defaultGeometry.getType().getCoordinateReferenceSystem();
    String srs = crsToSrs.computeIfAbsent(crs, this::toSrs);
    return new GeometryProperty(name, value, srs);
  }

  private String toSrs(CoordinateReferenceSystem crs) {
    boolean fullScan = true;
    try {
      return CRS.lookupIdentifier(crs, fullScan);
    } catch (FactoryException e) {
      log.warn("Unable to determine SRS identifer, will use null for {}", crs);
      return null;
    }
  }

}
