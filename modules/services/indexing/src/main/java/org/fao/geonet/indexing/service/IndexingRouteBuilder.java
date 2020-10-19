/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.indexing.service;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.GroupedBodyAggregationStrategy;
import org.fao.geonet.common.MetricUtil;
import org.fao.geonet.indexing.event.IndexEvent;
import org.fao.geonet.indexing.exception.IndexingRecordException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class IndexingRouteBuilder extends RouteBuilder {
  private static final String LOGGER_NAME = "org.fao.geonet.indexing.tasks";

  @Getter
  @Setter
  @Value("${gn.indexing.batch.size:100}")
  Integer indexingBatchSize;

  @Getter
  @Setter
  @Value("${gn.indexing.threadPool.size:20}")
  Integer indexingThreadPoolSize;

  @Override
  public void configure() throws Exception {
    //String lastIndexingDate = null;

    String metricBucketNumberOfRecordsFromDb =
        String.format("micrometer:counter:%s_%s",
            MetricUtil.METRIC_PREFIX,
            "indexing_${header.BUCKET}_numberOfRecordsFromDb");
    String metricBucketNumberOfRecordsIndexed =
        String.format("micrometer:counter:%s_%s",
            MetricUtil.METRIC_PREFIX,
            "indexing_${header.BUCKET}_numberOfRecordsIndexed");
    String metricBucketNumberOfRecordsWithErrors =
        String.format("micrometer:counter:%s_%s",
            MetricUtil.METRIC_PREFIX,
            "indexing_${header.BUCKET}_numberOfRecordsWithError");
    String metricBucketNumberOfRecordsWithUnsupportedSchema =
        String.format("micrometer:counter:%s_%s",
            MetricUtil.METRIC_PREFIX,
            "indexing_${header.BUCKET}_numberOfRecordsWithUnsupportedSchema");
    String metricBucketTimer =
        String.format("micrometer:timer:%s_%s",
            MetricUtil.METRIC_PREFIX,
            "indexing_${header.BUCKET}_timer");

    //    onException(IndexingRecordException.class)
    //        .continued(true)
    //        .log(LoggingLevel.ERROR, bucketLoggerName,
    //        "${header.BUCKET} / ${header.ID} / ${exception}")
    //        .to(metricBucketNumberOfRecordsWithError + "?increment=1");



    from("rest://get:index/{bucket}/{uuid}")
        .routeId("index-one-record")
        .routeGroup("gn-index")
        .routeDescription("API / Index one record")
        .setHeader("BUCKET", simple("${header.bucket}"))
        .setBody(header("uuid"))
        .log(LoggingLevel.INFO, LOGGER_NAME, "${header.BUCKET} / Indexing one record ${body} ...")
        .to("seda:index-now")
        .transform(constant("Record registered in queue"));


    from("rest://put:index/{bucket}?consumes=application/json")
        .id("index-many-records")
        .routeGroup("gn-index")
        .routeDescription("API / Index a bucket of records")
        .setHeader("BUCKET", simple("${header.bucket}"))
        .log(LoggingLevel.INFO, LOGGER_NAME, "${header.BUCKET} / Indexing records in bucket ...")
        .to("log:org.fao.geonet.indexing?level=DEBUG")
        .to("seda:register-to-indexing-queue")
        .transform(constant("All records registered from JSON in queue"));


    from("rest://get:index/{bucket}/all")
        .id("index-all-records")
        .transform()
        .simple("API / Indexing all records")
        .setHeader("BUCKET", simple("${header.bucket}"))
        .log(LoggingLevel.INFO, LOGGER_NAME, "${header.BUCKET} / Start indexing ...")
        .to(metricBucketTimer + "?action=start")
        .to("sql:SELECT id FROM metadata ORDER BY changedate?outputType=StreamList")
        .split(body())
          .setBody(simple("${body[id]}"))

          .to(metricBucketNumberOfRecordsFromDb + "?increment=1")
          .log(LoggingLevel.INFO, LOGGER_NAME,
              "${header.NUMBER_OF_RECORDS_STREAMED} = ${header.BUCKET} / ${body} / Indexing ...")
          .to("seda:register-to-indexing-queue")
        .end()
        .to(metricBucketTimer + "?action=stop")
        .transform(simple("Records registered in indexing queue"));

    from("rest://delete:index/records")
        .routeId("delete-records-index")
        .routeGroup("gn-index")
        .routeDescription("API / Delete index")
        .bean(IndexingService.class, "deleteIndex")
        .transform(constant("Index deleted."));


    from("seda:register-to-indexing-queue")
        .aggregate(header("BUCKET"), new GroupedBodyAggregationStrategy())
          .parallelProcessing()
          .completionSize(indexingBatchSize)
          .completionTimeout(10 * 1000)
          // TODO: What happens when last batch does not reach batch size - add a timeout?
          .to("seda:index-now");

    //    from("kafka:gn_indexing_tasks_stream")
    // from("rabbitmq:gn_indexing_tasks_stream?"
    //    + "exchangeType=topic&autoDelete=false&routingKey=gn_indexing_tasks_stream")
    from("spring-event:IndexEvent")
      .filter(exchange -> {
        IndexEvent event = exchange.getIn().getBody(IndexEvent.class);
        return event != null;
      })
      .log(LoggingLevel.INFO, LOGGER_NAME, "Processing incoming index event ${body}")
      .split()
        .jsonpath("uuid")
        .log(LoggingLevel.INFO, LOGGER_NAME, "uuid: ${body}")
        .setHeader("BUCKET", simple("${header.BUCKET}"))
        .log(LoggingLevel.INFO, LOGGER_NAME,
            "'${header.BUCKET}' / Indexing one record from event ${body} ...")
        .to("seda:index-now");

    from("seda:index-now")
      .threads(indexingThreadPoolSize)
      .log(LoggingLevel.INFO, LOGGER_NAME, "${threadName} ${header.BUCKET} / Indexing batch ...")
      .log(LoggingLevel.INFO, LOGGER_NAME, "${body}")
        .doTry()
          .bean(IndexingService.class, "indexRecords")
          .to(metricBucketNumberOfRecordsIndexed
              + "?increment=${header.NUMBER_OF_RECORDS_INDEXED}")
          .to(metricBucketNumberOfRecordsWithUnsupportedSchema
              + "?increment=${header.NUMBER_OF_RECORDS_WITH_UNSUPPORTED_SCHEMA}")
          .to(metricBucketNumberOfRecordsWithErrors
              + "?increment=${header.NUMBER_OF_RECORDS_WITH_ERRORS}")
          .log(LoggingLevel.INFO, LOGGER_NAME,
              "${threadName} ${header.BUCKET} / ${header.ID} / "
                  + "${header.NUMBER_OF_RECORDS_INDEXED} indexed.")
        .doCatch(IndexingRecordException.class)
          .log(LoggingLevel.ERROR, LOGGER_NAME,
              "${threadName} ${header.BUCKET} / ${header.ID} / ${exception}")
        .end()
        .end();
  }
}
