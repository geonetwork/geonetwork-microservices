/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.indexing.service;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;
import org.fao.geonet.common.XsltUtil;
import org.fao.geonet.domain.Metadata;
import org.fao.geonet.indexing.service.model.IndexRecord;
import org.fao.geonet.indexing.service.model.IndexRecords;
import org.fao.geonet.indexing.service.service.IndexingService;
import org.springframework.core.io.ClassPathResource;

class IndexRecordTest {

  @org.junit.jupiter.api.Test
  void testIso19139() throws IOException {
    String xmlFileContent =
        Files.readAllLines(new ClassPathResource(
            String.format("resources/metadata-%s.xml",
                "iso19139")
        ).getFile().toPath()).stream().collect(Collectors.joining());

    Metadata metadata = new Metadata();
    metadata.setId(999);
    metadata.setData(xmlFileContent);
    String record = IndexingService.collectDbProperties(metadata);
    IndexRecords results = XsltUtil.transformXmlToObject(
        String.format("<indexRecords>%s</indexRecords>", record),
        new ClassPathResource(
            "xslt/iso19139-index.xsl").getFile(),
        IndexRecords.class
    );
    assertEquals(1, results.getIndexRecord().size());

    IndexRecord result = results.getIndexRecord().get(0);
    assertEquals(999, result.getInternalId());
    assertEquals("metadata", result.getDocType());
    assertEquals("ISO 19115", result.getStandardName());
    assertEquals("ffc45d44-1cc2-4924-bb8c-214096eb9058", result.getMetadataIdentifier());
    assertEquals("fre", result.getMainLanguage());
    assertEquals(0, result.getOtherLanguage().size());
    assertNotNull(result.getIndexingDate());
    assertEquals("INSPIRE - Sites protégés en Wallonie (BE)", result.getResourceTitle().get("default"));

    ObjectMapper mapper = new ObjectMapper();
    System.out.println(mapper.writeValueAsString(result));
  }
}