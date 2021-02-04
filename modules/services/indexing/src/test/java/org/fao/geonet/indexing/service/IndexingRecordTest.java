/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.indexing.service;

import static org.fao.geonet.index.model.gn.IndexRecordFieldNames.CommonField.defaultText;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.fao.geonet.domain.Metadata;
import org.fao.geonet.index.JsonUtils;
import org.fao.geonet.index.model.gn.IndexDocumentType;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.index.model.gn.IndexRecords;
import org.fao.geonet.index.model.gn.IndexingReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ActiveProfiles("dev")
//@ComponentScan("org.fao.geonet")
class IndexingRecordTest {
  @Autowired
  IndexingService indexingService;

  @org.junit.jupiter.api.Test
  void testIso19139() throws IOException {
    String xmlFileContent =
        Files.readAllLines(new ClassPathResource(
            String.format("metadata-%s.xml",
                "iso19139")
        ).getFile().toPath()).stream().collect(Collectors.joining());

    Metadata metadata = new Metadata();
    metadata.setId(999);
    metadata.setData(xmlFileContent);

    List<Metadata> schemaRecords = new ArrayList<>();
    schemaRecords.add(metadata);

    IndexingReport report = new IndexingReport();

    IndexRecords results = new IndexingService().collectProperties(
        "iso19139",
        schemaRecords,
        report
    );

    assertEquals(1, results.getIndexRecord().size());

    assertEquals(0, report.getNumberOfGhostRecords());
    assertEquals(0, report.getNumberOfRecordsWithIndexingErrors());

    IndexRecord result = results.getIndexRecord().get(0);

    assertEquals(999, result.getInternalId());
    assertEquals(IndexDocumentType.metadata, result.getDocType());
    assertEquals("ISO 19115", result.getStandardName());
    assertEquals("ffc45d44-1cc2-4924-bb8c-214096eb9058", result.getMetadataIdentifier());
    assertEquals("fre", result.getMainLanguage());
    assertEquals(0, result.getOtherLanguage().size());
    assertNotNull(result.getIndexingDate());
    assertEquals("INSPIRE - Sites protégés en Wallonie (BE)", result.getResourceTitle().get(defaultText));

    //    ObjectMapper mapper = JsonUtils.getObjectMapper();
    //    System.out.println(mapper.writeValueAsString(result));
  }
}