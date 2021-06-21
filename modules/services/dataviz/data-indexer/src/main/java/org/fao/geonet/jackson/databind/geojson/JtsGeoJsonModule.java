/**
 * (c) 2021 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.jackson.databind.geojson;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.CoordinateSequenceFilter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.impl.PackedCoordinateSequenceFactory;

/**
 * Jackson {@link com.fasterxml.jackson.databind.Module} to handle GeoJSON
 * bindings for JTS {@link Geometry geometries}.
 * 
 * <p>
 * Code borrowed from the Geoserver-Cloud project.
 * 
 * <p>
 * When running a spring-boot application, being on the classpath should be
 * enough to get this module auto-registered to all {@link ObjectMapper}s, by
 * means of being registered under {@code
 * META-INF/services/com.fasterxml.jackson.databind.Module}.
 *
 * <p>
 * To register the module for a specific {@link ObjectMapper}, either:
 *
 * <pre>
 * <code>
 * ObjectMapper objectMapper = ...
 * objectMapper.findAndRegisterModules();
 * </code>
 * </pre>
 *
 * Or:
 *
 * <pre>
 * <code>
 * ObjectMapper objectMapper = ...
 * objectMapper.registerModule(new GeoToolsGeoJsonModule());
 * </code>
 * </pre>
 */
@Slf4j
public class JtsGeoJsonModule extends SimpleModule {
  private static final long serialVersionUID = 1L;

  private static final Version VERSION = new Version(1, 0, 0, null, null, null);

  public JtsGeoJsonModule() {
    super(JtsGeoJsonModule.class.getSimpleName(), VERSION);

    addSerializer(new GeometrySerializer());

    addDeserializer(Geometry.class, new GeometryDeserializer<>());
    addDeserializer(Point.class, new GeometryDeserializer<>());
    addDeserializer(MultiPoint.class, new GeometryDeserializer<>());
    addDeserializer(LineString.class, new GeometryDeserializer<>());
    addDeserializer(MultiLineString.class, new GeometryDeserializer<>());
    addDeserializer(Polygon.class, new GeometryDeserializer<>());
    addDeserializer(MultiPolygon.class, new GeometryDeserializer<>());
    addDeserializer(GeometryCollection.class, new GeometryDeserializer<>());
  }

  static class GeometrySerializer extends StdSerializer<Geometry> {
    private static final long serialVersionUID = 1L;

    public GeometrySerializer() {
      super(Geometry.class);
    }

    public @Override void serializeWithType(Geometry value, JsonGenerator gen,
        SerializerProvider serializers, TypeSerializer typeSer) throws IOException {

      WritableTypeId typeIdDef = typeSer.writeTypePrefix(gen,
          typeSer.typeId(value, JsonToken.START_OBJECT));

      if (value != null) {
        serializeContent(value, gen);
      }
      typeSer.writeTypeSuffix(gen, typeIdDef);
    }

    public @Override void serialize(Geometry value, JsonGenerator gen,
        SerializerProvider serializers) throws IOException {

      serialize(value, gen);
    }

    public void serialize(Geometry geometry, JsonGenerator generator) throws IOException {
      if (geometry == null) {
        generator.writeNull();
        return;
      }
      generator.writeStartObject();
      serializeContent(geometry, generator);
      generator.writeEndObject();
    }

    private void serializeContent(Geometry geometry, JsonGenerator generator) throws IOException {
      generator.writeStringField("type", geometry.getGeometryType());

      writeCustomFieldsFromUserData(geometry.getUserData(), generator);

      writeDimensions(geometry, generator);

      if (geometry instanceof GeometryCollection && !(geometry instanceof MultiPoint
          || geometry instanceof MultiLineString || geometry instanceof MultiPolygon)) {

        generator.writeFieldName("geometries");
        generator.writeStartArray();
        for (int i = 0; i < geometry.getNumGeometries(); i++) {
          serialize(geometry.getGeometryN(i), generator);
        }
        generator.writeEndArray();
        return;
      }
      generator.writeFieldName("coordinates");
      writeGeometry(geometry, generator);
    }

    private void writeCustomFieldsFromUserData(Object userData, JsonGenerator generator) {
      if (!(userData instanceof Map)) {
        return;
      }
      Map<?, ?> customProps = (Map<?, ?>) userData;
      customProps.forEach((key, val) -> {
        if (key instanceof String && ((String) key).startsWith("@")) {
          try {
            generator.writeObjectField((String) key, val);
          } catch (IOException e) {
            log.warn("Error encoding geometry custom property {}, property ignored", key, e);
          }
        }
      });
    }

    /**
     * Custom extension adding {@code dimensions (int)} and
     * {@code measures (boolean)} properties if the geometry has Z and/or M
     * ordinates.
     */
    private void writeDimensions(Geometry geometry, JsonGenerator generator) throws IOException {
      CoordinateSequence sampleSequence = findSampleSequence(geometry);
      if (sampleSequence != null && sampleSequence.getDimension() > 2) {
        int outputDimension = sampleSequence.getDimension();
        boolean hasM = sampleSequence.hasM();
        generator.writeNumberField("dimensions", outputDimension);
        generator.writeBooleanField("hasM", hasM);
      }
    }

    private CoordinateSequence findSampleSequence(Geometry g) {
      if (g == null || g.isEmpty())
        return null;
      if (g instanceof GeometryCollection) {
        return findSampleSequence(g.getGeometryN(0));
      }
      if (g instanceof org.locationtech.jts.geom.Point)
        return ((Point) g).getCoordinateSequence();
      if (g instanceof LineString)
        return ((LineString) g).getCoordinateSequence();
      if (g instanceof Polygon)
        return findSampleSequence(((Polygon) g).getExteriorRing());
      return null;
    }

    private void writeGeometry(Geometry geometry, JsonGenerator generator) throws IOException {
      if (geometry instanceof GeometryCollection) {
        writeMultiGeom((GeometryCollection) geometry, generator);
      } else {
        writeSimpleGeom(geometry, generator);
      }
    }

    private void writeMultiGeom(GeometryCollection multi, JsonGenerator generator)
        throws IOException {
      generator.writeStartArray();
      for (int i = 0; i < multi.getNumGeometries(); i++) {
        writeGeometry(multi.getGeometryN(i), generator);
      }
      generator.writeEndArray();
    }

    private void writeSimpleGeom(Geometry geometry, JsonGenerator generator) throws IOException {
      if (geometry.isEmpty()) {
        generator.writeStartArray();
        generator.writeEndArray();
        return;
      }
      if (geometry instanceof Point) {
        Point p = (Point) geometry;
        writeCoordinate(p.getCoordinateSequence(), 0, generator);
      } else if (geometry instanceof Polygon) {
        Polygon poly = (Polygon) geometry;
        generator.writeStartArray();
        writeCoordinateSequence(poly.getExteriorRing(), generator);
        for (int r = 0; r < poly.getNumInteriorRing(); r++) {
          writeCoordinateSequence(poly.getInteriorRingN(r), generator);
        }
        generator.writeEndArray();
      } else {
        writeCoordinateSequence(geometry, generator);
      }
    }

    private void writeCoordinateSequence(Geometry simpleGeom, JsonGenerator generator)
        throws IOException {
      final AtomicReference<CoordinateSequence> seqRef = new AtomicReference<>();
      simpleGeom.apply(new CoordinateSequenceFilter() {
        public @Override void filter(CoordinateSequence seq, int i) {
          seqRef.set(seq);
        }

        public @Override boolean isGeometryChanged() {
          return false;
        }

        public @Override boolean isDone() {
          return true;
        }
      });

      CoordinateSequence seq = seqRef.get();
      int size = seq.size();
      generator.writeStartArray();
      for (int i = 0; i < size; i++) {
        writeCoordinate(seq, i, generator);
      }
      generator.writeEndArray();
    }

    private void writeCoordinate(CoordinateSequence seq, int index, JsonGenerator generator)
        throws IOException {
      int dimension = seq.getDimension();
      generator.writeStartArray();
      for (int i = 0; i < dimension; i++)
        generator.writeNumber(seq.getOrdinate(index, i));
      generator.writeEndArray();
    }
  }

  static class GeometryDeserializer<T extends Geometry> extends JsonDeserializer<T> {

    private static final GeometryFactory DEFAULT_GF = new GeometryFactory(
        new PackedCoordinateSequenceFactory());

    private GeometryFactory geometryFactory;

    public GeometryDeserializer() {
      this(DEFAULT_GF);
    }

    public GeometryDeserializer(GeometryFactory geometryFactory) {
      this.geometryFactory = geometryFactory;
    }

    @SuppressWarnings("unchecked")
    public @Override T deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {

      return (T) readGeometry(p.readValueAsTree());
    }

    public Geometry readGeometry(ObjectNode geometryNode) {
      final int dimensions = getDimensions(geometryNode);
      final boolean hasM = resolveHasM(geometryNode);
      return readGeometry(geometryNode, dimensions, hasM);
    }

    private Geometry readGeometry(ObjectNode geometryNode, int dimensions, boolean hasM) {
      final String type = geometryNode.findValue("type").asText();
      Geometry geom;
      switch (type) {
      case Geometry.TYPENAME_POINT:
        geom = readPoint(geometryNode, dimensions, hasM);
        break;
      case Geometry.TYPENAME_MULTIPOINT:
        geom = readMultiPoint(geometryNode, dimensions, hasM);
        break;
      case Geometry.TYPENAME_LINESTRING:
        geom = readLineString(geometryNode, dimensions, hasM);
        break;
      case Geometry.TYPENAME_MULTILINESTRING:
        geom = readMultiLineString(geometryNode, dimensions, hasM);
        break;
      case Geometry.TYPENAME_POLYGON:
        geom = readPolygon(geometryNode, dimensions, hasM);
        break;
      case Geometry.TYPENAME_MULTIPOLYGON:
        geom = readMultiPolygon(geometryNode, dimensions, hasM);
        break;
      case Geometry.TYPENAME_GEOMETRYCOLLECTION:
        geom = readGeometryCollection(geometryNode, dimensions, hasM);
        break;
      default:
        throw new IllegalArgumentException("Unknown geometry node: " + geometryNode.toString());
      }

      Map<String, Object> userData = readUserDataMap(geometryNode);
      geom.setUserData(userData);
      return geom;
    }

    private Map<String, Object> readUserDataMap(ObjectNode geomNode) {
      Map<String, Object> userData = null;
      Iterator<String> fieldNames = geomNode.fieldNames();
      while (fieldNames.hasNext()) {
        String fieldName = fieldNames.next();
        if (fieldName.startsWith("@")) {
          JsonNode valueNode = geomNode.findValue(fieldName);
          if (valueNode instanceof ValueNode) {
            String value = ((ValueNode) valueNode).textValue();
            if (userData == null) {
              userData = new HashMap<>();
            }
            userData.put(fieldName, value);
          }
        }
      }
      return userData;
    }

    private int getDimensions(ObjectNode geometryNode) {
      JsonNode dimensionsProperty = geometryNode.findValue("dimensions");
      if (dimensionsProperty instanceof NumericNode) {
        return ((NumericNode) dimensionsProperty).asInt();
      }
      return 2;
    }

    private boolean resolveHasM(ObjectNode geometryNode) {
      JsonNode hasMProperty = geometryNode.findValue("hasM");
      if (hasMProperty instanceof BooleanNode) {
        return ((BooleanNode) hasMProperty).asBoolean();
      }
      return false;
    }

    private ArrayNode getCoordinatesNode(ObjectNode geometryNode) {
      JsonNode coordinatesNode = geometryNode.findValue("coordinates");
      ArrayNode coordinates;
      if (coordinatesNode == null || coordinatesNode.isNull()) {
        coordinates = new ArrayNode((JsonNodeFactory) null);
      } else if (coordinatesNode.isArray()) {
        coordinates = (ArrayNode) coordinatesNode;
      } else {
        throw new IllegalArgumentException(
            "Invalid 'coordinates' field type, expected ArrayNode, got "
                + coordinatesNode.getNodeType());
      }
      return coordinates;
    }

    private MultiLineString readMultiLineString(ObjectNode geometryNode, int dimensions,
        boolean hasM) {
      ArrayNode coordinates = getCoordinatesNode(geometryNode);
      if (coordinates.isEmpty()) {
        return geometryFactory.createMultiLineString();
      }

      LineString[] lineStrings = IntStream.range(0, coordinates.size())
          .mapToObj(i -> (ArrayNode) coordinates.get(i))
          .map(geomN -> readCoordinateSequence(geomN, dimensions, hasM))
          .map(geometryFactory::createLineString).toArray(LineString[]::new);

      return geometryFactory.createMultiLineString(lineStrings);
    }

    private MultiPolygon readMultiPolygon(ObjectNode geometryNode, int dimensions, boolean hasM) {
      ArrayNode coordinates = getCoordinatesNode(geometryNode);
      if (coordinates.isEmpty()) {
        return geometryFactory.createMultiPolygon();
      }
      Polygon[] polygons = IntStream.range(0, coordinates.size())
          .mapToObj(i -> (ArrayNode) coordinates.get(i))
          .map(array -> readPolygon(array, dimensions, hasM)).toArray(Polygon[]::new);
      return geometryFactory.createMultiPolygon(polygons);
    }

    private GeometryCollection readGeometryCollection(ObjectNode geometryNode, int dimensions,
        boolean hasM) {
      ArrayNode geometries = (ArrayNode) geometryNode.findValue("geometries");
      if (geometries.isEmpty()) {
        return geometryFactory.createGeometryCollection();
      }
      Geometry[] subGeometries = new Geometry[geometries.size()];
      for (int i = 0; i < geometries.size(); i++) {
        ObjectNode geomNode = (ObjectNode) geometries.get(i);
        subGeometries[i] = readGeometry(geomNode, dimensions, hasM);
      }
      return geometryFactory.createGeometryCollection(subGeometries);
    }

    private Polygon readPolygon(ObjectNode geometryNode, int dimensions, boolean hasM) {
      ArrayNode coordinates = getCoordinatesNode(geometryNode);
      return readPolygon(coordinates, dimensions, hasM);
    }

    private Polygon readPolygon(ArrayNode coordinates, int dimensions, boolean hasM) {
      if (null == coordinates || coordinates.isEmpty()) {
        return geometryFactory.createPolygon();
      }
      LinearRing shell = readLinearRing((ArrayNode) coordinates.get(0), dimensions, hasM);
      final LinearRing[] holes;
      if (coordinates.size() > 1) {
        holes = new LinearRing[coordinates.size() - 1];
        IntStream.range(1, coordinates.size()).forEach((i) -> {
          holes[i - 1] = readLinearRing((ArrayNode) coordinates.get(i), dimensions, hasM);
        });
      } else {
        holes = null;
      }
      return geometryFactory.createPolygon(shell, holes);
    }

    private LinearRing readLinearRing(ArrayNode coordinates, int dimensions, boolean hasM) {
      if (coordinates.isEmpty()) {
        return geometryFactory.createLinearRing();
      }
      CoordinateSequence coords = readCoordinateSequence(coordinates, dimensions, hasM);
      return geometryFactory.createLinearRing(coords);
    }

    private LineString readLineString(ObjectNode geometryNode, int dimensions, boolean hasM) {
      ArrayNode coordinates = getCoordinatesNode(geometryNode);
      if (coordinates.isEmpty()) {
        return geometryFactory.createLineString();
      }
      CoordinateSequence coords = readCoordinateSequence(coordinates, dimensions, hasM);
      return geometryFactory.createLineString(coords);
    }

    private MultiPoint readMultiPoint(ObjectNode geometryNode, int dimensions, boolean hasM) {
      ArrayNode coordinates = getCoordinatesNode(geometryNode);
      if (null == coordinates || coordinates.isEmpty()) {
        return geometryFactory.createMultiPoint();
      }
      CoordinateSequence coords = readCoordinateSequence(coordinates, dimensions, hasM);
      return geometryFactory.createMultiPoint(coords);
    }

    private CoordinateSequence readCoordinateSequence(ArrayNode coordinates, int dimension,
        boolean hasM) {
      final int size = coordinates.size();
      final int measures = hasM ? 1 : 0;
      CoordinateSequenceFactory sequenceFactory = geometryFactory.getCoordinateSequenceFactory();
      CoordinateSequence sequence = sequenceFactory.create(size, dimension, measures);
      for (int coord = 0; coord < size; coord++) {
        ArrayNode coordNode = (ArrayNode) coordinates.get(coord);
        for (int d = 0; d < dimension; d++) {
          sequence.setOrdinate(coord, d, coordNode.get(d).asDouble());
        }
      }
      return sequence;
    }

    private Point readPoint(ObjectNode geometryNode, int dimensions, boolean hasM) {
      ArrayNode coordinateArray = getCoordinatesNode(geometryNode);
      if (null == coordinateArray || coordinateArray.isEmpty()) {
        return geometryFactory.createPoint();
      }
      CoordinateSequence coordinate = geometryFactory.getCoordinateSequenceFactory().create(1,
          dimensions, hasM ? 1 : 0);
      for (int d = 0; d < dimensions; d++)
        coordinate.setOrdinate(0, d, coordinateArray.get(d).asDouble());
      return geometryFactory.createPoint(coordinate);
    }

    public static boolean isGeometry(JsonNode value) {
      if (!(value instanceof ObjectNode)) {
        return false;
      }
      JsonNode typeNode = value.get("type");
      return typeNode instanceof TextNode && isGeometry(((TextNode) typeNode).asText());
    }

    private static final Set<String> geomTypes = new HashSet<>(Arrays.asList( //
        "Point", "MultiPoint", "LineString", "MultiLineString", "Polygon", "MultiPolygon",
        "GeometryCollection"));

    public static boolean isGeometry(String type) {
      return geomTypes.contains(type);
    }
  }

}
