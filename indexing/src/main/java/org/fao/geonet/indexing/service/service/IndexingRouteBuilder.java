/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.indexing.service.service;

import lombok.Getter;
import lombok.Setter;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.GroupedBodyAggregationStrategy;
import org.fao.geonet.common.MetricUtil;
import org.fao.geonet.indexing.service.exception.IndexingRecordException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndexingRouteBuilder extends RouteBuilder {
  private static final String LOGGER_NAME = "org.fao.geonet.indexing.tasks";

  @Getter
  @Setter
  @Value("${gn.indexing.batch.size:20}")
  Integer indexingBatchSize;

  @Override
  public void configure() throws Exception {
    String lastIndexingDate = null;

    String metricBucketNumberOfRecordsFromDb =
        String.format("micrometer:counter:%s_%s",
            MetricUtil.METRIC_PREFIX,
            "indexing_${header.BUCKET}_numberOfRecordsFromDb");
    String metricBucketNumberOfRecordsIndexed =
        String.format("micrometer:counter:%s_%s",
            MetricUtil.METRIC_PREFIX,
            "indexing_${header.BUCKET}_numberOfRecordsIndexed");
    String metricBucketNumberOfRecordsWithError =
        String.format("micrometer:counter:%s_%s",
            MetricUtil.METRIC_PREFIX,
            "indexing_${header.BUCKET}_numberOfRecordsWithError");
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
          .log(LoggingLevel.INFO, LOGGER_NAME, "${header.BUCKET} / ${body} / Indexing ...")
          .to("seda:register-to-indexing-queue")
        .end()
        .to(metricBucketTimer + "?action=stop")
        .transform(constant("Registerd all records in queue"));

    from("rest://delete:index/records")
        .routeId("delete-records-index")
        .routeGroup("gn-index")
        .routeDescription("API / Delete index")
        .bean(IndexingService.class, "deleteIndex")
        .transform(constant("Index deleted."));


    from("seda:register-to-indexing-queue")
        .aggregate(header("BUCKET"), new GroupedBodyAggregationStrategy())
          .completionSize(indexingBatchSize)
          // TODO: What happens when last batch does not reach batch size - add a timeout?
          .to("seda:index-now");


    from("seda:index-now")
        .log(LoggingLevel.INFO, LOGGER_NAME, "${header.BUCKET} / Indexing batch ...")
        .log(LoggingLevel.INFO, LOGGER_NAME, "${body}")
        .doTry()
          .bean(IndexingService.class, "indexRecords")
          .to(metricBucketNumberOfRecordsIndexed + "?increment=1")
          .log(LoggingLevel.INFO, LOGGER_NAME, "${header.BUCKET} / ${header.ID} / Completed.")
        .doCatch(IndexingRecordException.class)
          .log(LoggingLevel.ERROR, LOGGER_NAME, "${header.BUCKET} / ${header.ID} / ${exception}")
          .to(metricBucketNumberOfRecordsWithError + "?increment=1")
        .end()
        .end();
  }
}
