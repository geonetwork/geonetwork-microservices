package org.fao.geonet.index.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class IndexRecordTest {

  @Test
  public void testJsonToPojo() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    String json = Files.readString(
        new ClassPathResource("index-document.json").getFile().toPath());
    JsonNode jsonNode = objectMapper.readTree(json);

    try {
      IndexRecord record = objectMapper.readValue(
          jsonNode.get("_source").toPrettyString(),
          IndexRecord.class);
      Assert.assertEquals(
          "High Resolution Layer: Water and Wetness 2015 (raster 100m), Mar. 2018",
          record.resourceTitle.get("default")
          );
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }



  @Test
  public void testJsonToJsonLd() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();

    String json = Files.readString(
        new ClassPathResource("index-document.json").getFile().toPath());
    JsonNode jsonNode = objectMapper.readTree(json);

    try {
      IndexRecord record = objectMapper.readValue(
          jsonNode.get("_source").toPrettyString(),
          IndexRecord.class);
      Assert.assertEquals(
          "High Resolution Layer: Water and Wetness 2015 (raster 100m), Mar. 2018",
          record.resourceTitle.get("default")
      );

      JsonLdRecord jsonLdRecord = new JsonLdRecord(record);
      Assert.assertEquals("",
          jsonLdRecord.toString());
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }
}