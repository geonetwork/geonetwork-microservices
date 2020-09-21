package org.fao.geonet.indexing.service;

import static org.apache.camel.language.groovy.GroovyLanguage.groovy;

import org.apache.camel.builder.RouteBuilder;
import org.elasticsearch.client.Client;
import org.springframework.stereotype.Component;

@Component
public class IndexingRouteBuilder extends RouteBuilder {

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

    from("rest://get:index/{bucket}/all")
        .id("index-all-records")
        .transform()
        .simple("Indexing all")
        //        .to("micrometer:")
        .to("log:org.fao.geonet.indexing?level=DEBUG")
        .to("sql:SELECT id FROM metadata ORDER BY changedate?outputType=StreamList")
        .split(body())
          .streaming()
          .setHeader("BUCKET", simple("${header.bucket}"))
          .setHeader("ID", simple("${body[id]}"))
          .to("log:org.fao.geonet.indexing?level=DEBUG")
          //          .setBody(groovy("headers.bucket + ',' + body.get('id')"))
          //          .to("log:org.fao.geonet.indexing?level=DEBUG")
          //          .setHeader("ID", body().getExpression())
          .to("seda:register-to-indexing-queue")
        .end()
        .transform(constant("Registerd all records in queue"));

    from("seda:register-to-indexing-queue")
        .transform()
        .simple("Indexing ${header.ID} queue in bucket ${header.BUCKET}")
        //        .to("log:org.fao.geonet.indexing?level=DEBUG")
        //  WHERE id IN (${})
        .to("log:org.fao.geonet.indexing?level=DEBUG")

        .bean(IndexingService.class, "indexRecord")
        //          .to("mock:result")
        .to("log:org.fao.geonet.indexing?level=DEBUG")
        .end();;
  }
}
