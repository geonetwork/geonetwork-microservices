package org.fao.geonet.jackson.databind.geojson;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.common.collect.ImmutableMap;
import org.fao.geonet.dataviz.model.GeodataRecord;
import org.fao.geonet.dataviz.model.GeometryProperty;
import org.fao.geonet.dataviz.model.SimpleProperty;
import org.locationtech.jts.geom.Geometry;

public class GeoRecordToGeoJsonFeatureModule extends SimpleModule {
  private static final long serialVersionUID = 1L;

  private static final Version VERSION = new Version(1, 0, 0, null, null, null);

  public GeoRecordToGeoJsonFeatureModule() {
    super(GeoRecordToGeoJsonFeatureModule.class.getSimpleName(), VERSION);

    addSerializer(new GeodataRecordSerializer());
  }

  static class GeodataRecordSerializer extends StdSerializer<GeodataRecord> {
    private static final long serialVersionUID = 1L;

    public GeodataRecordSerializer() {
      super(GeodataRecord.class);
    }

    public @Override void serializeWithType(GeodataRecord record, JsonGenerator gen,
        SerializerProvider serializers, TypeSerializer typeSer) throws IOException {

      WritableTypeId typeIdDef = typeSer.writeTypePrefix(gen,
          typeSer.typeId(record, JsonToken.START_OBJECT));

      serializeContent(record, gen);

      typeSer.writeTypeSuffix(gen, typeIdDef);
    }

    public @Override void serialize(GeodataRecord record, JsonGenerator generator,
        SerializerProvider serializers) throws IOException {

      if (record == null) {
        generator.writeNull();
        return;
      }
      generator.writeStartObject();
      serializeContent(record, generator);
      generator.writeEndObject();
    }

    private void serializeContent(GeodataRecord record, JsonGenerator generator)
        throws IOException {

      generator.writeStringField("type", "Feature");
      if (null != record.getId()) {
        generator.writeStringField("@id", record.getId());
      }
      writeGeometryProperty(generator, record.getGeometry());
      writeProperties(generator, record.getProperties());
    }

    private void writeProperties(JsonGenerator generator, List<SimpleProperty<?>> properties)
        throws IOException {
      generator.writeFieldName("properties");
      generator.writeStartObject();
      for (SimpleProperty<?> p : properties) {
        generator.writeObjectField(p.getName(), p.getValue());
      }
      generator.writeEndObject();
    }

    private void writeGeometryProperty(JsonGenerator generator, GeometryProperty geometryProp)
        throws IOException {

      Geometry value = geometryProp.getValue();
      if (null == geometryProp || value == null) {
        generator.writeNullField("geometry");
      } else {
        String name = geometryProp.getName();
        String srs = geometryProp.getSrs();
        Object oldUserData = value.getUserData();
        Map<String, String> customProperties = ImmutableMap.of("@name", name, "@srs", srs);
        value.setUserData(customProperties);
        try {
          generator.writeObjectField("geometry", value);
        } finally {
          value.setUserData(oldUserData);
        }
      }
    }
  }
}
