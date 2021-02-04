/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.gn;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;

public class LocationDeserializer extends JsonDeserializer<List<Coordinate>> {

  private String separator = ",";

  @Override
  public List<Coordinate> deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException {
    ObjectCodec oc = p.getCodec();
    JsonNode node = oc.readTree(p);
    List<Coordinate> geometries = new ArrayList<>();
    if (node.isArray()) {
      node.elements().forEachRemaining(c -> {
        String[] coords = c.textValue().split(separator);
        geometries.add(new Coordinate(Double.valueOf(coords[0]), Double.valueOf(coords[1]), 0));
      });
    } else if (node.isTextual()) {
      String text = node.asText();
      String[] coords = text.split(separator);
      if (coords.length == 2) {
        geometries.add(new Coordinate(Double.valueOf(coords[0]), Double.valueOf(coords[1]), 0));
      }
    }
    return geometries;
  }
}
