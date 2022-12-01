package org.fao.geonet.common.search.processor.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.common.search.domain.ReservedOperation;
import org.fao.geonet.common.search.domain.UserInfo;
import org.fao.geonet.index.JsonUtils;
import org.fao.geonet.index.converter.GeoJsonConverter;
import org.fao.geonet.index.model.geojson.Record;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j(topic = "org.fao.geonet.searching")
@Component("GeoJsonResponseProcessorImpl")
public class GeoJsonResponseProcessorImpl
    extends JsonUserAndSelectionAwareResponseProcessorImpl {

  @Autowired
  GeoJsonConverter geoJsonConverter;

  @Override
  public void processResponse(HttpSession httpSession,
      InputStream streamFromServer, OutputStream streamToClient,
      UserInfo userInfo, String bucket, Boolean addPermissions) throws Exception {

    ObjectMapper objectMapper = JsonUtils.getObjectMapper();
    JsonParser parser = parserForStream(streamFromServer);
    JsonGenerator generator = ResponseParser.jsonFactory.createGenerator(streamToClient);

    try {
      ResponseParser responseParser = new ResponseParser();
      generator.writeStartObject();

      generator.writeStringField("type", "FeatureCollection");
      generator.writeArrayFieldStart("features");
      {
        responseParser.matchHits(parser, generator, doc -> {

          // Remove fields with privileges info
          if (doc.has(IndexRecordFieldNames.source)) {
            ObjectNode sourceNode = (ObjectNode) doc.get(IndexRecordFieldNames.source);

            for (ReservedOperation o : ReservedOperation.values()) {
              sourceNode.remove(IndexRecordFieldNames.opPrefix + o.getId());
            }

            IndexRecord indexRecord = objectMapper.readValue(
                doc.get(IndexRecordFieldNames.source).toPrettyString(),
                IndexRecord.class);
            try {
              Record geojsonRecord = geoJsonConverter.convert(indexRecord);
              generator.writeRawValue(objectMapper.writeValueAsString(geojsonRecord));
            } catch (Exception ex) {
              log.error(String.format(
                  "GeoJSON conversion returned null result for uuid %s. Check http://localhost:9901/collections/main/items/%s?f=geojson",
                  doc.get("_id").asText(), doc.get("_id").asText()));
            }
          }
        }, false);
      }
      generator.writeEndArray();
      generator.writeNumberField("size", responseParser.total);
      generator.writeEndObject();
      generator.flush();
    } finally {
      generator.close();
    }
  }
}
