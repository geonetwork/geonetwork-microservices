/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.gn;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;

public class LocationSerializer extends JsonSerializer<List<Coordinate>> {

  @Override
  public void serialize(
      List<Coordinate> coordinate,
      JsonGenerator gen,
      SerializerProvider serializers)
      throws IOException {
    gen.writeStartObject();
    gen.writeArrayFieldStart(IndexRecordFieldNames.location);
    coordinate.forEach(c -> {
      try {
        gen.writeStartArray();
        gen.writeRawValue(c.getX() + "," + c.getY());
        gen.writeEndArray();
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
    });
    gen.writeEndArray();
    gen.writeEndObject();
  }
}
