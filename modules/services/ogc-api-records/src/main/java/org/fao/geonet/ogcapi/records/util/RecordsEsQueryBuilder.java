package org.fao.geonet.ogcapi.records.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.geometry.Rectangle;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.fao.geonet.common.search.SearchConfiguration;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@Component
@ConstructorBinding
@Slf4j(topic = "org.fao.geonet.ogcapi")
public class RecordsEsQueryBuilder {

  @Autowired
  private SearchConfiguration configuration;

  // TODO: Sources depends on output type
  private static List<String> defaultSources = Arrays.asList(
      "resourceTitleObject", "resourceAbstractObject",
      "resourceType", "resourceDate",
      "id", "metadataIdentifier", "schema",
      "link", "allKeywords",
      "contact", "contactForResource",
      "cl_status",
      "edit", "tag", "changeDate",
      "createDate", "mainLanguage", "geom", "formats");

  private static final String SORT_BY_SEPARATOR = ",";

  private static String defaultSpatialOperation = "intersects";

  public RecordsEsQueryBuilder(SearchConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * Creates a ElasticSearch query for a single record.
   *
   * @param uuid             Record uuid.
   * @param collectionFilter Filter to select the record in a collection scope.
   * @param includes         List of fields to return (null, retuns all).
   * @return ElasticSearch query.
   */
  public String buildQuerySingleRecord(String uuid,
      String collectionFilter,
      List<String> includes) {

    return String.format("{\"from\": %d, \"size\": %d, "
            + "\"query\": {\"query_string\": "
            + "{\"query\": \"+_id:\\\"%s\\\" %s %s\"}}, "
            + "\"_source\": {\"includes\": [%s]}}",
        0, 1, uuid,
        collectionFilter == null ? "" : collectionFilter,
        configuration.getQueryFilter(),
        includes == null ? "\"*\""
            : includes.stream().collect(
                Collectors.joining("\", \"", "\"", "\""))
    );
  }

  /**
   * Creates a ElasticSearch query from Records API parameters.
   */
  public String buildQuery(
      List<String> q,
      List<String> externalids,
      List<BigDecimal> bbox,
      Integer startIndex, Integer limit,
      String collectionFilter,
      List<String> sortBy,
      Set<String> sourceFields) {
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    sourceBuilder.from(startIndex).size(limit);

    if (sortBy != null) {
      sortBy.forEach(s -> Stream.of(s.split(SORT_BY_SEPARATOR))
          .forEach(order -> {
            boolean isDescending = order.startsWith("-");
            sourceBuilder.sort(
                new FieldSortBuilder(order.replaceAll("^[\\+-]", ""))
                    .order(
                        isDescending ? SortOrder.DESC : SortOrder.ASC));
          }));
    }

    Set<String> sources = new HashSet<>(defaultSources);
    if (sourceFields != null) {
      sources.addAll(sourceFields);
    } else {
      sources.addAll(configuration.getSources());
    }
    sourceBuilder.fetchSource(sources.toArray(new String[]{}), null);

    BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

    QueryBuilder fullTextQuery =
        QueryBuilders.queryStringQuery(buildFullTextSearchQuery(q));
    boolQuery.must(fullTextQuery);

    if (externalids != null && !externalids.isEmpty()) {
      boolQuery.must(
          QueryBuilders.termsQuery(
              IndexRecordFieldNames.uuid,
              externalids));
    }

    GeoShapeQueryBuilder geoQuery;
    if (bbox != null && bbox.size() == 4) {
      Rectangle rectangle = new Rectangle(
          bbox.get(0).doubleValue(),
          bbox.get(2).doubleValue(),
          bbox.get(3).doubleValue(),
          bbox.get(1).doubleValue());

      try {
        geoQuery = QueryBuilders
            .geoShapeQuery(IndexRecordFieldNames.geom, rectangle)
            .relation(ShapeRelation.getRelationByName(defaultSpatialOperation));
        boolQuery.must(geoQuery);
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
    }

    String filterQueryString = configuration.getQueryFilter();
    if (StringUtils.isNotEmpty(collectionFilter)) {
      filterQueryString += " " + collectionFilter;
    }
    boolQuery.filter(QueryBuilders.queryStringQuery(filterQueryString));
    sourceBuilder.query(boolQuery);
    sourceBuilder.trackTotalHits(configuration.getTrackTotalHits());
    log.debug("OGC API query: {}", sourceBuilder.toString());

    return sourceBuilder.toString();
  }

  private String buildFullTextSearchQuery(List<String> q) {
    String queryString = "*:*";
    if (q != null && !q.isEmpty()) {
      String values = q.stream().collect(Collectors.joining(" AND "));
      if (StringUtils.isNotEmpty(configuration.getQueryBase())) {
        queryString = configuration.getQueryBase().replaceAll(
            "\\$\\{any\\}",
            values);
      } else {
        queryString = values;
      }
    }
    return queryString;
  }
}
