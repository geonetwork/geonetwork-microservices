package org.fao.geonet.indexing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import org.fao.geonet.common.XsltUtil;
import org.fao.geonet.indexing.model.IndexRecord;
import org.springframework.core.io.ClassPathResource;

class IndexRecordTest {

  @org.junit.jupiter.api.Test
  void testIso19139() throws IOException {
    IndexRecord result = XsltUtil.transformXmlToObject(
        "<MD_Metadata/>",
        new ClassPathResource(
            "xslt/iso19139-index.xsl").getFile(),
        IndexRecord.class
    );
    assertEquals("metadata", result.getDocType());
  }
}