package org.fao.geonet.common.search.processor.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.common.search.domain.ReservedOperation;
import org.fao.geonet.common.search.domain.UserInfo;
import org.fao.geonet.index.JsonUtils;
import org.fao.geonet.index.converter.SchemaOrgConverter;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames;
import org.springframework.stereotype.Component;


@Slf4j(topic = "org.fao.geonet.searching")
@Component("JsonLdResponseProcessorImpl")
public class JsonLdResponseProcessorImpl
    extends JsonUserAndSelectionAwareResponseProcessorImpl {

  @Override
  public void processResponse(HttpSession httpSession,
      InputStream streamFromServer, OutputStream streamToClient,
      UserInfo userInfo, String bucket, Boolean addPermissions) throws Exception {

    ObjectMapper objectMapper = JsonUtils.getObjectMapper();
    JsonParser parser = parserForStream(streamFromServer);
    JsonGenerator generator = ResponseParser.jsonFactory.createGenerator(streamToClient);

    // TODO: Check to enable it
    //final Set<String> selections = (addPermissions ?
    //    SelectionManager.getManager(ApiUtils.getUserSession(httpSession)).getSelection(bucket)
    //    : new HashSet<>());
    Set<String> selections = new HashSet<>();
    ResponseParser responseParser = new ResponseParser();
    generator.writeStartObject();
    // https://schema.org/DataFeed
    generator.writeStringField("@context", "https://schema.org/");
    generator.writeStringField("@type", "DataFeed");
    // name
    // url
    // thumbnailUrl
    // description
    // contentReferenceTime
    // position ie. startindex?
    generator.writeArrayFieldStart("dataFeedElement");
    {
      responseParser.matchHits(parser, generator, doc -> {
        if (addPermissions) {
          addUserInfo(doc, userInfo);
          addSelectionInfo(doc, selections);
        }

        // Remove fields with privileges info
        if (doc.has(IndexRecordFieldNames.source)) {
          ObjectNode sourceNode = (ObjectNode) doc.get(IndexRecordFieldNames.source);

          for (ReservedOperation o : ReservedOperation.values()) {
            sourceNode.remove(IndexRecordFieldNames.opPrefix + o.getId());
          }

          IndexRecord record = objectMapper.readValue(
              doc.get(IndexRecordFieldNames.source).toPrettyString(),
              IndexRecord.class);
          try {
            ObjectNode node = SchemaOrgConverter.convert(record);
            generator.writeRawValue(node.toString());
          } catch (Exception ex) {
            log.error(String.format(
                "JSON-LD conversion returned null result for uuid %s. Check http://localhost:9901/collections/main/items/%s?f=schema.org",
                doc.get("_id").asText(), doc.get("_id").asText()));
          }
        }
      }, false);
    }
    generator.writeEndArray();
    //    generator.writeNumberField("took", 0);
    generator.writeNumberField("size", responseParser.total);
    generator.writeEndObject();
    generator.flush();
    generator.close();
  }
}
