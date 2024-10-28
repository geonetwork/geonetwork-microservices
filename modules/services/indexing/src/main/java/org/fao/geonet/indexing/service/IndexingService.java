/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.indexing.service;

import static org.elasticsearch.rest.RestStatus.CREATED;
import static org.elasticsearch.rest.RestStatus.OK;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.fao.geonet.common.xml.XsltUtil;
import org.fao.geonet.domain.AbstractMetadata;
import org.fao.geonet.domain.Metadata;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.index.model.gn.IndexRecords;
import org.fao.geonet.index.model.gn.IndexingReport;
import org.fao.geonet.indexing.exception.IndexingRecordException;
import org.fao.geonet.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "org.fao.geonet.indexing.tasks")
public class IndexingService {

  @Getter
  @Setter
  @Value("${gn.index.records:gn-cloud-records}")
  String index;

  @Autowired
  MetadataRepository metadataRepository;

  @Autowired
  RestHighLevelClient client;

  /**
   * Delete index.
   */
  public void deleteIndex(Exchange e) {
    try {
      AcknowledgedResponse deleteIndexResponse = client.indices()
          .delete(new DeleteIndexRequest(index), RequestOptions.DEFAULT);
      if (deleteIndexResponse.isAcknowledged()) {
        log.info(String.format(
            "Index %s removed.",
            index));
      }
    } catch (IOException ioException) {
      log.warn(String.format(
          "Index %s does not exist.",
          index));
    }
  }

  /**
   * Read record from database, convert it to {@see IndexRecord} and save it index.
   */
  public void indexRecords(Exchange e) throws IndexingRecordException {
    Object body = e.getIn().getBody();

    IndexingReport report = new IndexingReport();

    List<Integer> ids = new ArrayList<>();
    if (body instanceof List) {
      ids = (List<Integer>) body;
    } else if (body instanceof String) {
      ids.add(Integer.parseInt((String) body));
    }
    List<Metadata> records = metadataRepository.findAllById(ids);

    log.info(String.format(
        "Indexing %d records in batch", ids.size()
    ));

    if (ids.size() != records.size()) {
      List<Integer> listOfIds = records.stream().map(Metadata::getId)
          .collect(Collectors.toList());
      List<Integer> ghost = new ArrayList<Integer>(ids);
      ghost.removeAll(listOfIds);
      log.warn(String.format(
          "Error while retrieving records from database. "
              + "%d record(s) missing. Records are %s."
              + "Records may have been deleted since we started this indexing task.",
          ghost.size(),
          ghost.toString()
      ));
      report.setNumberOfGhostRecords(ghost.size());
      e.getIn().setHeader("NUMBER_OF_GHOST", report.getNumberOfGhostRecords());
    }

    Map<String, List<Metadata>> recordsBySchema = records.stream()
        .collect(Collectors.groupingBy(record -> record.getDataInfo().getSchemaId()));

    recordsBySchema.forEach((schema, schemaRecords) -> {
      log.info(String.format(
          "Indexing %d records in schema %s", schemaRecords.size(), schema
      ));
      IndexRecords indexRecords = collectProperties(schema, schemaRecords, report);
      if (indexRecords.getIndexRecord() != null
          && indexRecords.getIndexRecord().size() > 0) {
        sendToIndex(indexRecords, report);
      }
    });

    e.getIn().setHeader("NUMBER_OF_RECORDS_INDEXED",
        records.size());
    e.getIn().setHeader("NUMBER_OF_GHOST_RECORDS",
        report.getNumberOfGhostRecords());
    e.getIn().setHeader("NUMBER_OF_RECORDS_WITH_ERRORS",
        report.getNumberOfRecordsWithIndexingErrors());
    e.getIn().setHeader("NUMBER_OF_RECORDS_WITH_UNSUPPORTED_SCHEMA",
        report.getNumberOfRecordsWithUnsupportedSchema());
  }


  protected IndexRecords collectProperties(
      String schema,
      List<Metadata> schemaRecords,
      IndexingReport report) {
    String indexingXsltFileName = String.format(
        "xslt/%s-index.xsl",
        schema);
    IndexRecords indexRecords = new IndexRecords();
    try {
      File indexingXsltFile = new ClassPathResource(indexingXsltFileName).getFile();

      String recordsAsString = schemaRecords
          .stream()
          .map(IndexingService::collectDbProperties)
          .collect(Collectors.joining(""));
      indexRecords = XsltUtil.transformXmlToObject(
          String.format("<indexRecords>%s</indexRecords>", recordsAsString),
          indexingXsltFile,
          IndexRecords.class
      );
    } catch (IOException ioException) {
      report.setNumberOfRecordsWithUnsupportedSchema(schemaRecords.size());
      log.error(String.format(
          "Schema %s used by records %s does not exist or does not provide indexing file %s.",
          schema,
          schemaRecords.stream()
              .map(AbstractMetadata::getId)
              .map(Objects::toString)
              .collect(Collectors.joining(",")),
          indexingXsltFileName
      ));

    }
    return indexRecords;
  }

  /**
   * Initialize an {@link IndexRecord} with all properties from the
   * database of an {@link AbstractMetadata}
   * and return its XML representation as string.
   */
  protected static String collectDbProperties(AbstractMetadata r) {
    IndexRecord indexRecord = new IndexRecord(r);
    StringWriter sw = new StringWriter();
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(IndexRecord.class);
      Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
      marshaller.marshal(indexRecord, sw);
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    return sw.toString();
  }


  private BulkRequest buildBulkRequest(IndexRecords indexRecords) {
    BulkRequest bulkRequest = new BulkRequest(index);
    ObjectMapper mapper = new ObjectMapper();

    indexRecords.getIndexRecord().forEach(r -> {
      try {
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.id(r.getId());
        indexRequest.source(mapper.writeValueAsString(r), XContentType.JSON);
        bulkRequest.add(indexRequest);
      } catch (JsonProcessingException jsonProcessingException) {
        jsonProcessingException.printStackTrace();
      }
    });
    return bulkRequest;
  }

  private void sendToIndex(IndexRecords indexRecords,
      IndexingReport report) {
    BulkRequest bulkRequest = buildBulkRequest(indexRecords);
    try {
      // TODO: Asynchronous?
      BulkResponse bulkItemResponses = client.bulk(bulkRequest, RequestOptions.DEFAULT);
      log.info(String.format(
          "Indexing operation took %d.",
          bulkItemResponses.getIngestTookInMillis()
      ));
      if (bulkItemResponses.hasFailures()) {
        AtomicInteger failureCount = new AtomicInteger();
        Arrays.stream(bulkItemResponses.getItems()).forEach(item -> {
          if (item.status() != OK
              && item.status() != CREATED) {
            failureCount.getAndIncrement();
            // TODO: Index error document
          }
        });
        report.setNumberOfRecordsWithIndexingErrors(failureCount.intValue());
        log.info(String.format(
            "Indexing operation has failures %d.",
            failureCount
        ));
      }
    } catch (ElasticsearchStatusException indexException) {
      report.setNumberOfRecordsWithIndexingErrors(indexRecords.getIndexRecord().size());
      log.error(String.format(
          "Error while saving records %d in index. Error is: %s.",
          indexException.getMessage()
      ));
    } catch (IOException ioException) {
      log.error(String.format(
          "Error while sending records to index. Error is: %s.",
          ioException.getMessage()
      ));
    }
  }
}
