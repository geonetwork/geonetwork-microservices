/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.fao.geonet.indexing.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.micrometer.MicrometerConstants;
import org.fao.geonet.common.MetricUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class IndexingRouteBuilder extends RouteBuilder {

  //  @Getter
  //  @Setter
  //  @Value("${gn.indexing.batch.size:20}")
  //  Integer indexingBatchSize;

  @Override
  public void configure() throws Exception {
    String lastIndexingDate = null;

    from("rest://get:index/{bucket}/{uuid}")
        .id("index-one-record")
        .transform()
        .simple("Indexing ${header.uuid} from bucket ${header.bucket}")
        .setHeader("ID", header("uuid"))
        .to("log:org.fao.geonet.indexing?level=DEBUG")
        .to("seda:register-to-indexing-queue");

    from("rest://put:index/{bucket}?consumes=application/json")
        .id("index-many-records")
        .transform()
        .simple("Indexing ${header.uuid}")
        .to("log:org.fao.geonet.indexing?level=DEBUG")
        .to("seda:register-to-indexing-queue")
        .transform(constant("Registerd all records from JSON in queue"));

    String metricBucketNumberOfRecordFromDb =
        String.format("micrometer:counter:%s_%s",
            MetricUtil.METRIC_PREFIX,
            "indexing_${header.BUCKET}_numberOfRecordFromDb");
    String metricBucketNumberOfRecordIndexed =
        String.format("micrometer:counter:%s_%s",
            MetricUtil.METRIC_PREFIX,
            "indexing_${header.BUCKET}_numberOfRecordIndexed");
    String metricBucketTimer =
        String.format("micrometer:timer:%s_%s",
            MetricUtil.METRIC_PREFIX,
            "indexing_${header.BUCKET}_timer");

    from("rest://get:index/{bucket}/all")
        .id("index-all-records")
        .transform()
        .simple("Indexing all")
        .setHeader("BUCKET", simple("${header.bucket}"))
        .to(metricBucketTimer + "?action=start")
        .to("log:org.fao.geonet.indexing?level=DEBUG")
        .to("sql:SELECT id FROM metadata ORDER BY changedate?outputType=StreamList")
        .split(body())
          .streaming()
          .setHeader("ID", simple("${body[id]}"))
          .to(metricBucketNumberOfRecordFromDb + "?increment=1")
          .to("log:org.fao.geonet.indexing?level=DEBUG")
          //          .setBody(groovy("headers.bucket + ',' + body.get('id')"))
          //          .to("log:org.fao.geonet.indexing?level=DEBUG")
          //          .setHeader("ID", body().getExpression())
          .to("seda:register-to-indexing-queue")
        .end()
        .to(metricBucketTimer + "?action=stop")
        .transform(constant("Registerd all records in queue"));

    from("seda:register-to-indexing-queue")
        .transform()
        .simple("Indexing ${header.ID} queue in bucket ${header.BUCKET}")
        //        .to("log:org.fao.geonet.indexing?level=DEBUG")
        //  WHERE id IN (${})
        .to("log:org.fao.geonet.indexing?level=DEBUG")

        .bean(IndexingService.class, "indexRecord")
        //          .to("mock:result")
        .to(metricBucketNumberOfRecordIndexed + "?increment=1")
        .to("log:org.fao.geonet.indexing?level=DEBUG")
        .end();
  }
}
