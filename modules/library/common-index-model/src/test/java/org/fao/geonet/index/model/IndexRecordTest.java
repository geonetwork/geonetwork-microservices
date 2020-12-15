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
      Assert.assertEquals("{\n"
              + "  \"@context\" : \"http://schema.org/\",\n"
              + "  \"@type\" : \"dataset\",\n"
              + "  \"@id\" : \"8108e203-59db-4672-b9e0-c1863fd6523b\",\n"
              + "  \"name\" : \"High Resolution Layer: Water and Wetness 2015 (raster 100m), Mar. 2018\",\n"
              + "  \"description\" : \"The combined Water and Wetness (WAW) product is a thematic product showing the occurrence of water and wet surfaces over the period from 2009 to 2015 for the EEA39 area. This metadata corresponds to the aggregation of the 20m classified product into a 100m raster. \\nTwo WAW products are available:\\n- The main Water and Wetness (WAW) product, with defined classes of (1) permanent water, (2) temporary water, (3) permanent wetness and (4) temporary wetness.\\n- The additional expert product: Water and Wetness Probability Index (WWPI).\\nThe products show the occurrence of water and indicate the degree of wetness in a physical sense, assessed independently of the actual vegetation cover and are thus not limited to a specific land cover class and their relative frequencies.\\nThe production of the High Resolution Water and Wetness layers was coordinated by the European Environment Agency (EEA) in the frame of the EU Copernicus programme.\\nData is provided as a mosaic of the full area, and as tiles with a side length of 1000 km x 1000 km.\",\n"
              + "  \"dateCreated\" : \"2020-11-25T12:36:21\",\n"
              + "  \"dateModified\" : \"2020-11-25T12:36:21\"\n"
              + "}",
          jsonLdRecord.toString());
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }
}