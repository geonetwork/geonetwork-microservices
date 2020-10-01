/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.fao.geonet.indexing.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.camel.Exchange;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.fao.geonet.common.XsltUtil;
import org.fao.geonet.domain.Metadata;
import org.fao.geonet.indexing.model.IndexRecord;
import org.fao.geonet.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class IndexingService {

  @Autowired
  MetadataRepository metadataRepository;

  @Autowired
  RestHighLevelClient client;

  /**
   * Read record from database, convert it to {@see IndexRecord}
   * and save it index.
   */
  public void indexRecord(Exchange e) {
    Integer id = (Integer) e.getIn().getHeader("ID");
    Metadata record = metadataRepository.findOneById(id);
    if (record == null) {
       // ghost record
    } else {
      System.out.println(record.getUuid());
      try {
        IndexRecord result = XsltUtil.transformXmlToObject(
            record.getData(),
            new ClassPathResource(
                String.format("xslt/%s-index.xsl",
                    record.getDataInfo().getSchemaId())
            ).getFile(),
            IndexRecord.class
        );

        IndexRequest request = new IndexRequest("test");
        request.id(record.getUuid());

        ObjectMapper mapper = new ObjectMapper();
        request.source(mapper.writeValueAsString(result), XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
        e.getIn().setBody(result);
      } catch (IOException ioException) {
        // Schema not supported
        ioException.printStackTrace();
      }
    }
  }
}
