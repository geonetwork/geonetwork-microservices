package org.fao.geonet.common.search.processor.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.fao.geonet.common.search.domain.ReservedOperation;
import org.fao.geonet.common.search.domain.UserInfo;
import org.fao.geonet.index.JsonUtils;
import org.fao.geonet.index.converter.JsonLdRecord;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

@Component("JsonLdResponseProcessorImpl")
public class JsonLdResponseProcessorImpl
    extends JsonUserAndSelectionAwareResponseProcessorImpl {

  @Override
  public void processResponse(HttpSession httpSession,
      InputStream streamFromServer, OutputStream streamToClient,
      UserInfo userInfo, String bucket, boolean addPermissions) throws Exception {

    ObjectMapper objectMapper = JsonUtils.getObjectMapper();
    JsonParser parser = ResponseParser.jsonFactory.createParser(streamFromServer);
    JsonGenerator generator = ResponseParser.jsonFactory.createGenerator(streamToClient);
    parser.nextToken();  //Go to the first token

    // TODO: Check to enable it
    //final Set<String> selections = (addPermissions ?
    //    SelectionManager.getManager(ApiUtils.getUserSession(httpSession)).getSelection(bucket)
    //    : new HashSet<>());
    Set<String> selections = new HashSet<>();

    generator.writeStartObject();
    generator.writeNumberField("took", 0);
    generator.writeNumberField("total", 0);
    generator.writeArrayFieldStart("items");
    {
      new ResponseParser().matchHits(parser, generator, doc -> {
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
          JsonLdRecord jsonLdRecord = new JsonLdRecord(record);
          System.out.println(jsonLdRecord.toString());
          generator.writeRawValue(jsonLdRecord.toString());
        }
      }, false);
    }
    generator.writeEndArray();
    generator.writeEndObject();
    generator.flush();
    generator.close();
  }
}
