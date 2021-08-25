package org.fao.geonet.dataviz.jackson.geojson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.dataviz.model.GeodataRecord;
import org.fao.geonet.dataviz.model.GeometryProperty;
import org.fao.geonet.dataviz.model.SimpleProperty;
import org.locationtech.jts.geom.Geometry;

@Slf4j
public class GeoRecordToGeoJsonFeatureModule extends SimpleModule {
  private static final long serialVersionUID = 1L;

  private static final Version VERSION = new Version(1, 0, 0, null, null, null);

  public GeoRecordToGeoJsonFeatureModule() {
    super(GeoRecordToGeoJsonFeatureModule.class.getSimpleName(), VERSION);

    addSerializer(new GeodataRecordSerializer());
    addDeserializer(GeodataRecord.class, new GeodataRecordDeserializer());
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
      if (null != record.getTypeName()) {
        generator.writeStringField("@typeName", record.getTypeName());
      }
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

      if (null == geometryProp || geometryProp.getValue() == null) {
        generator.writeNullField("geometry");
      } else {
        String name = geometryProp.getName();
        String srs = geometryProp.getSrs();
        Geometry value = geometryProp.getValue();

        Object oldUserData = value.getUserData();
        value.setUserData(createNameAndSrsUserData(name, srs));
        try {
          generator.writeObjectField("geometry", value);
        } finally {
          value.setUserData(oldUserData);
        }
      }
    }

    private Map<String, String> createNameAndSrsUserData(String name, String srs) {
      Map<String, String> customProperties = new HashMap<>(2);
      customProperties.put("@name", name);
      customProperties.put("@srs", srs);
      return customProperties;
    }
  }

  static class GeodataRecordDeserializer extends JsonDeserializer<GeodataRecord> {

    public @Override GeodataRecord deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {

      GeodataRecord.Builder builder = GeodataRecord.builder();
      String fieldName;
      while (null != (fieldName = p.nextFieldName())) {
        switch (fieldName) {
        case "type":
          String type = p.nextTextValue();
          if (!"Feature".equals(type)) {
            log.info("GeoJSON Feature type attribute is not 'Feature': {}", type);
          }
          break;
        case "@typeName":
          String typeName = p.nextTextValue();
          builder.typeName(typeName);
          break;
        case "@id":
          String fid = p.nextTextValue();
          builder.id(fid);
          break;
        case "geometry":
          JsonToken nextValue = p.nextValue();
          if (nextValue == JsonToken.START_OBJECT) {
            GeometryProperty geom = readGeometryProperty(p);
            builder.geometry(geom);
          } else if (nextValue == JsonToken.VALUE_NULL) {
            builder.geometry(null);
          }
          break;
        case "properties":
          List<SimpleProperty<?>> properties = Collections.emptyList();
          nextValue = p.nextValue();
          if (nextValue == JsonToken.START_OBJECT) {
            properties = readProperties(p);
          }
          builder.properties(properties);
          break;
        default:
          log.info("Unknown GeoJSON Feature object field '{}'", fieldName);
          break;
        }
      }
      return builder.build();
    }

    private List<SimpleProperty<?>> readProperties(JsonParser p) throws IOException {
      List<SimpleProperty<?>> props = new ArrayList<>();
      String propName;
      while (null != (propName = p.nextFieldName())) {
        Object value;
        JsonToken nextValue = p.nextValue();
        switch (nextValue) {
        case START_ARRAY:
          value = p.readValueAs(List.class);
          break;
        case START_OBJECT:
          value = p.readValueAs(Map.class);
          break;
        case VALUE_TRUE:
          value = Boolean.TRUE;
          break;
        case VALUE_FALSE:
          value = Boolean.FALSE;
          break;
        case VALUE_NULL:
          value = null;
          break;
        case VALUE_NUMBER_FLOAT:
          value = p.getFloatValue();
          break;
        case VALUE_NUMBER_INT:
          value = p.getIntValue();
          break;
        case VALUE_STRING:
          value = p.getValueAsString();
          break;
        case NOT_AVAILABLE:
        case VALUE_EMBEDDED_OBJECT:
        default:
          log.warn("Unknown or unsupported value type {} for property {}", nextValue, propName);
          value = null;
          break;
        }
        props.add(SimpleProperty.builder().name(propName).value(value).build());
      }
      return props;
    }

    private GeometryProperty readGeometryProperty(JsonParser p) throws IOException {
      Geometry geom = p.readValueAs(Geometry.class);
      if (geom == null) {
        return null;
      }
      GeometryProperty.Builder builder = GeometryProperty.builder();
      builder.value(geom);

      Object userData = geom.getUserData();
      if (userData instanceof Map) {
        Map<?, ?> extraProps = (Map<?, ?>) userData;
        builder.name(getStringProp("@name", extraProps));
        builder.srs(getStringProp("@srs", extraProps));
        if (extraProps.size() == 2 && extraProps.containsKey("@name")
            && extraProps.containsKey("@srs")) {
          geom.setUserData(null);
        } else if (!extraProps.isEmpty()) {
          log.info("Geometry has additional user data properties, leaving it intact: {}",
              extraProps.keySet());
        }
      }
      return builder.build();
    }

    private String getStringProp(String propName, Map<?, ?> extraProps) {
      Object value = extraProps.get(propName);
      if (value instanceof String) {
        return (String) value;
      } else if (value != null) {
        log.info("Geoemtry userData is a Map and contains {} but its not a String: {}", propName,
            value.getClass().getCanonicalName());
      }
      return null;
    }
  }
}
