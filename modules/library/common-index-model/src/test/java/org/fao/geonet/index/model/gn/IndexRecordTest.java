package org.fao.geonet.index.model.gn;

import static org.fao.geonet.index.model.gn.IndexRecordFieldNames.CommonField.defaultText;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import org.fao.geonet.domain.MetadataType;
import org.fao.geonet.index.JsonUtils;
import org.fao.geonet.index.converter.DcatConverter;
import org.fao.geonet.index.converter.FormatterConfiguration;
import org.fao.geonet.index.converter.SchemaOrgConverter;
import org.fao.geonet.index.model.dcat2.CatalogRecord;
import org.fao.geonet.index.model.dcat2.DataService;
import org.fao.geonet.index.model.dcat2.Dataset;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DcatConverter.class})
public class IndexRecordTest {

  @MockBean
  FormatterConfiguration configuration;


  @Autowired
  DcatConverter dcatConverter;

  @BeforeEach
  public void setUp() {
    given(this.configuration.buildLandingPageLink("")).willReturn("");
  }

  @Test
  public void testJsonToPojo() throws IOException {
    ObjectMapper objectMapper = JsonUtils.getObjectMapper();
    String json = Files.readString(
        new ClassPathResource("index-document.json").getFile().toPath());
    JsonNode jsonNode = objectMapper.readTree(json);

    try {
      IndexRecord record = objectMapper.readValue(
          jsonNode.get(IndexRecordFieldNames.source).toPrettyString(),
          IndexRecord.class);
      Assert.assertEquals(
          "High Resolution Layer: Water and Wetness 2015 (raster 100m), Mar. 2018",
          record.resourceTitle.get(defaultText)
      );

      Assert.assertEquals(49, record.getOtherProperties().size());

      Assert.assertEquals("gmd:MD_Metadata", record.getRoot());

      Overview o = record.getOverview().get(0);
      Assert.assertEquals(
          "https://sdi.eea.europa.eu/public/catalogue-graphic-overview/8108e203-59db-4672-b9e0-c1863fd6523b.png",
          o.getUrl());
      Assert.assertEquals("Global overview", o.getLabel().get("default"));

      String org = record.getOrg().get(0);
      Assert.assertEquals("European Environment Agency", org);

      Assert.assertEquals(MetadataType.METADATA.code, record.getIsTemplate().charValue());
      Assert.assertEquals(IndexDocumentType.metadata, record.getDocType());

      List<ResourceDate> dates = record.getResourceDate();
      Assert.assertEquals(2, dates.size());
      Assert.assertEquals("creation", dates.get(0).getType());
      Assert.assertEquals("2018-03-22", dates.get(0).getDate());

      List<Link> links = record.getLinks();
      Assert.assertEquals(1, links.size());
      Assert.assertEquals(
          "https://land.copernicus.eu/pan-european/high-resolution-layers/water-wetness/status-maps/2015/view",
          links.get(0).getUrl().get("default"));

      List<Coordinate> locations = record.getLocations();
      Assert.assertEquals(47.10185, locations.get(0).getX(), .0001);
      Assert.assertEquals(-22.3441, locations.get(0).getY(), .0001);

      Assert.assertEquals(
          "{\"type\":\"Polygon\",\"coordinates\":[[[-31.2684,27.6375],[-13.4198,27.6375],[-13.4198,66.5662],[-31.2684,66.5662],[-31.2684,27.6375]]]}",
          record.getGeometries().get(0));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }

  @Test
  public void testServiceJsonToDcatXML() throws IOException {
    ObjectMapper objectMapper = JsonUtils.getObjectMapper();
    String json = Files.readString(
        new ClassPathResource("index-document-service.json").getFile().toPath());
    JsonNode jsonNode = objectMapper.readTree(json);

    try {
      IndexRecord record = objectMapper.readValue(
          jsonNode.get(IndexRecordFieldNames.source).toPrettyString(),
          IndexRecord.class);
      Assert.assertEquals(
          "Zones de distribution en eau (ZDE) - Service de visualisation REST",
          record.resourceTitle.get(defaultText)
      );

      JAXBContext context = null;
      context = JAXBContext.newInstance(
          CatalogRecord.class, Dataset.class, DataService.class);
      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

      CatalogRecord catalogRecord = dcatConverter.convert(jsonNode);
      StringWriter sw = new StringWriter();
      marshaller.marshal(catalogRecord, sw);

    } catch (JsonProcessingException e) {
      e.printStackTrace();
      Assert.fail();
    } catch (PropertyException e) {
      e.printStackTrace();
      Assert.fail();
    } catch (JAXBException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }


  @Test
  public void testJsonToJsonLd() throws IOException {
    ObjectMapper objectMapper = JsonUtils.getObjectMapper();

    String json = Files.readString(
        new ClassPathResource("index-document.json").getFile().toPath());
    JsonNode jsonNode = objectMapper.readTree(json);

    try {
      IndexRecord record = objectMapper.readValue(
          jsonNode.get(IndexRecordFieldNames.source).toPrettyString(),
          IndexRecord.class);
      Assert.assertEquals(
          "High Resolution Layer: Water and Wetness 2015 (raster 100m), Mar. 2018",
          record.resourceTitle.get(defaultText)
      );

      ObjectNode jsonLdDocument = SchemaOrgConverter.convert(record);
      //      Assert.assertEquals("{\n"
      //              + "  \"@context\" : \"http://schema.org/\",\n"
      //              + "  \"@type\" : \"dataset\",\n"
      //              + "  \"@id\" : \"8108e203-59db-4672-b9e0-c1863fd6523b\",\n"
      //              + "  \"name\" : \"High Resolution Layer: Water and Wetness 2015 (raster 100m), Mar. 2018\",\n"
      //              + "  \"description\" : \"The combined Water and Wetness (WAW) product is a thematic product showing the occurrence of water and wet surfaces over the period from 2009 to 2015 for the EEA39 area. This metadata corresponds to the aggregation of the 20m classified product into a 100m raster. \\nTwo WAW products are available:\\n- The main Water and Wetness (WAW) product, with defined classes of (1) permanent water, (2) temporary water, (3) permanent wetness and (4) temporary wetness.\\n- The additional expert product: Water and Wetness Probability Index (WWPI).\\nThe products show the occurrence of water and indicate the degree of wetness in a physical sense, assessed independently of the actual vegetation cover and are thus not limited to a specific land cover class and their relative frequencies.\\nThe production of the High Resolution Water and Wetness layers was coordinated by the European Environment Agency (EEA) in the frame of the EU Copernicus programme.\\nData is provided as a mosaic of the full area, and as tiles with a side length of 1000 km x 1000 km.\",\n"
      //              + "  \"dateCreated\" : \"2020-11-25T12:36:21\",\n"
      //              + "  \"dateModified\" : \"2020-11-25T12:36:21\"\n"
      //              + "}",
      //          jsonLdRecord.toString());
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      Assert.fail();
    }
  }


}