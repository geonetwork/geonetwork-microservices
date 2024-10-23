package org.fao.geonet.common.search.processor.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.common.search.domain.ReservedOperation;
import org.fao.geonet.common.search.domain.UserInfo;
import org.fao.geonet.index.JsonUtils;
import org.fao.geonet.index.converter.IGeoJsonConverter;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Slf4j(topic = "org.fao.geonet.searching")
@Component("GeoJsonResponseProcessorImpl")
public class GeoJsonResponseProcessorImpl
    extends JsonUserAndSelectionAwareResponseProcessorImpl {

  @Autowired
  IGeoJsonConverter geoJsonConverter;

  @Override
  public void processResponse(HttpSession httpSession,
      InputStream streamFromServer, OutputStream streamToClient,
      UserInfo userInfo, String bucket, Boolean addPermissions) throws Exception {

    ObjectMapper objectMapper = JsonUtils.getObjectMapper();
    JsonParser parser = parserForStream(streamFromServer);
    JsonGenerator generator = ResponseParser.jsonFactory.createGenerator(streamToClient);

    try {
      ResponseParser elasticJsonResponseParser = new ResponseParser();
      generator.writeStartObject();

      generator.writeStringField("type", "FeatureCollection");
      generator.writeArrayFieldStart("features");
      AtomicInteger numbFeatures = new AtomicInteger(0);
      {
        elasticJsonResponseParser.matchHits(parser, generator, doc -> {

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
              Object geojsonRecord =   geoJsonConverter.convert(indexRecord);
              generator.writeRawValue(objectMapper.writeValueAsString(geojsonRecord));
              numbFeatures.incrementAndGet();
            } catch (Exception ex) {
              log.error(String.format(
                  "GeoJSON conversion returned null result for uuid %s. Check http://localhost:9901/collections/main/items/%s?f=geojson",
                  doc.get("_id").asText(), doc.get("_id").asText()));
            }
          }
        }, false);
      }
      generator.writeEndArray();
      generator.writeNumberField("numberMatched", elasticJsonResponseParser.total);
      generator.writeNumberField("numberReturned", numbFeatures.intValue());

      generator.writeStringField("timeStamp", Instant.now().toString());

      generator.writeArrayFieldStart("links");
      //TO DO ADD LINKS
      generator.writeEndArray();

      generator.writeEndObject();
      generator.flush();
    } finally {
      generator.close();
    }
  }
}
