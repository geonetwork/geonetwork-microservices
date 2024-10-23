/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.geometry.Rectangle;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.fao.geonet.ogcapi.records.controller.Query;
import org.fao.geonet.ogcapi.records.model.GnElasticInfo;
import org.fao.geonet.ogcapi.records.model.GnElasticInfo.ElasticColumnType;
import org.fao.geonet.ogcapi.records.model.GnElasticInfo.ElasticQueryType;
import org.fao.geonet.ogcapi.records.model.JsonProperty;
import org.fao.geonet.ogcapi.records.model.JsonSchema;
import org.junit.Test;


/**
 * These test cases are quite complex because they are doing the full-process instead of very small
 * parts.
 * <p>
 * 1. They build a queryables Service that has the test-case defined Queryables + this is done so we
 * can more easily control the queryable configuration + this is done via a mock 2. We then create a
 * Query 3. We then use `QueryToElastic` to add the queryable elastic query to the main
 * (SearchSourceBuilder) query.
 * <p>
 * 4. We then check to see if the added query makes sense.
 * <p>
 * <p>
 * NOTE: IF THESE TEST CASES FAIL, IT COULD BE THAT THE `QUERYTOELASTIC` CLASS WAS MODIFIED TO
 * PRODUCE A "BETTER" QUERY.  THESE TEST CASES SHOULD BE UPDATED.  MOSTLY THIS WOULD INVOLVE THE
 * VERY END OF THE TEST CASE - THE PART THAT VERIFIES THE ADDED QUERY!
 */
public class QueryToElasticTest {


  public QueryablesService initQueryablesService(String pname, JsonProperty property) {

    var queryables = new JsonSchema();
    queryables.setProperties(new LinkedHashMap<>());
    queryables.getProperties().put(pname, property);

    QueryablesService queryablesService = new QueryablesService() {
      public JsonSchema buildQueryables(String collectionId) {
        return queryables;
      }
    };



    return queryablesService;
  }

  /**
   * tests the "id" - should result in a simple multi-match
   */
  @Test
  public void test_id() {

    //setup queryable
    var jsonProperty = new JsonProperty();
    var paths = new ArrayList<GnElasticInfo>();
    var info1 = new GnElasticInfo("uuid", ElasticColumnType.KEYWORD);
    paths.add(info1);
    jsonProperty.setxGnElasticPath(paths);
    var queryableService = initQueryablesService("id", jsonProperty);

    //setup service
    QueryToElastic queryToElastic = new QueryToElastic();
    queryToElastic.queryablesService = queryableService;

    //create simple query for id=abc
    var query = buildQuery(queryableService, "id", "abc");

    //the QueryToElastic builds on top of an already existing SearchSourceBuilder
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    //the mainquery for the source builder is always a boolQuery
    var mainQuery = QueryBuilders.boolQuery();
    sourceBuilder.query(mainQuery);

    //add the queryables search to the boolQuery
    queryToElastic.augmentWithQueryables(sourceBuilder, query);

    //extract just the created query
    var createdQuery = mainQuery.must().get(0);

    //test the created elastic query
    //should be a multimatch (default match type)
    // for field "uuid",
    // query text is "abc"
    // and minimumShouldMatch should be 1.
    assertEquals(MultiMatchQueryBuilder.class, createdQuery.getClass());
    var multiMatch = (MultiMatchQueryBuilder) createdQuery;

    assertEquals("abc", multiMatch.value());
    assertTrue(multiMatch.fields().containsKey("uuid"));
    assertEquals(1, multiMatch.fields().size());

    assertEquals("1", multiMatch.minimumShouldMatch());
  }

  /**
   * tests the "title" - should result in a   multi-match with two columns. also, check for
   * multi-lingual expansion.
   */
  @Test
  public void test_multi() {

    //setup queryable
    var jsonProperty = new JsonProperty();
    var paths = new ArrayList<GnElasticInfo>();
    var info1 = new GnElasticInfo("resourceTitleObject.default",
        ElasticColumnType.TEXT);
    paths.add(info1);
    var info2 = new GnElasticInfo("resourceTitleObject.lang${lang3iso}",
        ElasticColumnType.TEXT);
    paths.add(info2);
    jsonProperty.setxGnElasticPath(paths);
    var queryableService = initQueryablesService("title", jsonProperty);

    //setup service
    QueryToElastic queryToElastic = new QueryToElastic();
    queryToElastic.queryablesService = queryableService;

    //create simple query for id=abc
    var query = buildQuery(queryableService, "title", "abc");

    //the QueryToElastic builds on top of an already existing SearchSourceBuilder
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    //the mainquery for the source builder is always a boolQuery
    var mainQuery = QueryBuilders.boolQuery();
    sourceBuilder.query(mainQuery);

    //add the queryables search to the boolQuery
    queryToElastic.augmentWithQueryables(sourceBuilder, query);

    //extract just the created query
    var createdQuery = mainQuery.must().get(0);

    //test the created elastic query
    //should be a multimatch (default match type)
    // for field "uuid",
    // query text is "abc"
    // and minimumShouldMatch should be 1.
    assertEquals(MultiMatchQueryBuilder.class, createdQuery.getClass());
    var multiMatch = (MultiMatchQueryBuilder) createdQuery;

    assertEquals("abc", multiMatch.value());
    assertTrue(multiMatch.fields().containsKey("resourceTitleObject.default"));

    //if this fails, it could be doing "better" multilingual language type injection
    assertTrue(multiMatch.fields().containsKey("resourceTitleObject.lang*"));
    assertEquals(2, multiMatch.fields().size());

    assertEquals("1", multiMatch.minimumShouldMatch());
  }

  /**
   * tests date types - should result in a range query
   */
  @Test
  public void test_date() {

    //setup queryable
    var jsonProperty = new JsonProperty();
    var paths = new ArrayList<GnElasticInfo>();
    var info1 = new GnElasticInfo("created", ElasticColumnType.DATE);
    paths.add(info1);
    jsonProperty.setxGnElasticPath(paths);
    var queryableService = initQueryablesService("created", jsonProperty);

    //setup service
    QueryToElastic queryToElastic = new QueryToElastic();
    queryToElastic.queryablesService = queryableService;

    //create simple query for id=abc
    var query = buildQuery(queryableService, "created",
        "2023-10-22T21:10:03Z/2024-10-22T21:10:03Z");

    //the QueryToElastic builds on top of an already existing SearchSourceBuilder
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    //the mainquery for the source builder is always a boolQuery
    var mainQuery = QueryBuilders.boolQuery();
    sourceBuilder.query(mainQuery);

    //add the queryables search to the boolQuery
    queryToElastic.augmentWithQueryables(sourceBuilder, query);

    //extract just the created query
    var createdQuery = mainQuery.must().get(0);

    //test the created elastic query
    //should be a RangeQueryBuilder
    // for field "created",
    // from: "2023-10-22T21:10:03Z"
    //  to :2024-10-22T21:10:03Z
    assertEquals(RangeQueryBuilder.class, createdQuery.getClass());
    var rangeQueryBuilder = (RangeQueryBuilder) createdQuery;
    assertEquals("created", rangeQueryBuilder.fieldName());

    assertEquals("2023-10-22T21:10:03Z", rangeQueryBuilder.from());
    assertEquals("2024-10-22T21:10:03Z", rangeQueryBuilder.to());
  }

  /**
   * tests date types - should result in a range query, with from=null
   */
  @Test
  public void test_date_nolower() {

    //setup queryable
    var jsonProperty = new JsonProperty();
    var paths = new ArrayList<GnElasticInfo>();
    var info1 = new GnElasticInfo("created", ElasticColumnType.DATE);
    paths.add(info1);
    jsonProperty.setxGnElasticPath(paths);
    var queryableService = initQueryablesService("created", jsonProperty);

    //setup service
    QueryToElastic queryToElastic = new QueryToElastic();
    queryToElastic.queryablesService = queryableService;

    //create simple query for id=abc
    var query = buildQuery(queryableService, "created",
        "../2024-10-22T21:10:03Z");

    //the QueryToElastic builds on top of an already existing SearchSourceBuilder
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    //the mainquery for the source builder is always a boolQuery
    var mainQuery = QueryBuilders.boolQuery();
    sourceBuilder.query(mainQuery);

    //add the queryables search to the boolQuery
    queryToElastic.augmentWithQueryables(sourceBuilder, query);

    //extract just the created query
    var createdQuery = mainQuery.must().get(0);

    //test the created elastic query
    //should be a RangeQueryBuilder
    // for field "created",
    // from: "2023-10-22T21:10:03Z"
    //  to :2024-10-22T21:10:03Z
    assertEquals(RangeQueryBuilder.class, createdQuery.getClass());
    var rangeQueryBuilder = (RangeQueryBuilder) createdQuery;
    assertEquals("created", rangeQueryBuilder.fieldName());

    assertNull(rangeQueryBuilder.from());
    assertEquals("2024-10-22T21:10:03Z", rangeQueryBuilder.to());
  }

  /**
   * tests date types - should result in a range query, with to=null
   */
  @Test
  public void test_date_noupper() {

    //setup queryable
    var jsonProperty = new JsonProperty();
    var paths = new ArrayList<GnElasticInfo>();
    var info1 = new GnElasticInfo("created", ElasticColumnType.DATE);
    paths.add(info1);
    jsonProperty.setxGnElasticPath(paths);
    var queryableService = initQueryablesService("created", jsonProperty);

    //setup service
    QueryToElastic queryToElastic = new QueryToElastic();
    queryToElastic.queryablesService = queryableService;

    //create simple query for id=abc
    var query = buildQuery(queryableService, "created",
        "2023-10-22T21:10:03Z/..");

    //the QueryToElastic builds on top of an already existing SearchSourceBuilder
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    //the mainquery for the source builder is always a boolQuery
    var mainQuery = QueryBuilders.boolQuery();
    sourceBuilder.query(mainQuery);

    //add the queryables search to the boolQuery
    queryToElastic.augmentWithQueryables(sourceBuilder, query);

    //extract just the created query
    var createdQuery = mainQuery.must().get(0);

    //test the created elastic query
    //should be a RangeQueryBuilder
    // for field "created",
    // from: "2023-10-22T21:10:03Z"
    //  to :2024-10-22T21:10:03Z
    assertEquals(RangeQueryBuilder.class, createdQuery.getClass());
    var rangeQueryBuilder = (RangeQueryBuilder) createdQuery;
    assertEquals("created", rangeQueryBuilder.fieldName());

    assertNull(rangeQueryBuilder.to());
    assertEquals("2023-10-22T21:10:03Z", rangeQueryBuilder.from());
  }

  /**
   * tests date types - should result in a range query
   */
  @Test
  public void test_geo() {

    //setup queryable
    var jsonProperty = new JsonProperty();
    var paths = new ArrayList<GnElasticInfo>();
    var info1 = new GnElasticInfo("geo", ElasticColumnType.GEO);
    paths.add(info1);
    jsonProperty.setxGnElasticPath(paths);
    var queryableService = initQueryablesService("geo", jsonProperty);

    //setup service
    QueryToElastic queryToElastic = new QueryToElastic();
    queryToElastic.queryablesService = queryableService;

    //create simple query for id=abc
    var query = buildQuery(queryableService, "geo",
        "1,2,11,12");

    //the QueryToElastic builds on top of an already existing SearchSourceBuilder
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    //the mainquery for the source builder is always a boolQuery
    var mainQuery = QueryBuilders.boolQuery();
    sourceBuilder.query(mainQuery);

    //add the queryables search to the boolQuery
    queryToElastic.augmentWithQueryables(sourceBuilder, query);

    //extract just the created query
    var createdQuery = mainQuery.must().get(0);

    //test the created elastic query
    //should be a
    // for field "geom",

    assertEquals(GeoShapeQueryBuilder.class, createdQuery.getClass());
    var geoShapeQueryBuilder = (GeoShapeQueryBuilder) createdQuery;

    assertEquals("geo", geoShapeQueryBuilder.fieldName());
    assertEquals(ShapeRelation.INTERSECTS, geoShapeQueryBuilder.relation());

    assertEquals(Rectangle.class, geoShapeQueryBuilder.shape().getClass());
    var rect = (Rectangle) geoShapeQueryBuilder.shape();
    assertEquals(1, rect.getMinX(), 0);
    assertEquals(2, rect.getMinY(), 0);
    assertEquals(11, rect.getMaxX(), 0);
    assertEquals(12, rect.getMaxY(), 0);
  }


  /**
   * tests nested query types - should result in a nested query, inside and OR boolean
   */
  @Test
  public void test_nested() {

    //setup queryable
    var jsonProperty = new JsonProperty();
    var paths = new ArrayList<GnElasticInfo>();
    var info1 = new GnElasticInfo("contact.organisationObject.default",
        ElasticColumnType.TEXT);
    info1.setElasticQueryType(ElasticQueryType.NESTED);
    paths.add(info1);
    var info2 = new GnElasticInfo("contact.organisationObject.lang${lang3iso}",
        ElasticColumnType.TEXT);
    info2.setElasticQueryType(ElasticQueryType.NESTED);
    paths.add(info2);
    jsonProperty.setxGnElasticPath(paths);
    var queryableService = initQueryablesService("contacts", jsonProperty);

    //setup service
    QueryToElastic queryToElastic = new QueryToElastic();
    queryToElastic.queryablesService = queryableService;

    //create simple query for id=abc
    var query = buildQuery(queryableService, "contacts",
        "jody");

    //the QueryToElastic builds on top of an already existing SearchSourceBuilder
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    //the mainquery for the source builder is always a boolQuery
    var mainQuery = QueryBuilders.boolQuery();
    sourceBuilder.query(mainQuery);

    //add the queryables search to the boolQuery
    queryToElastic.augmentWithQueryables(sourceBuilder, query);

    //extract just the created query
    var createdQuery = mainQuery.must().get(0);

    assertEquals(BoolQueryBuilder.class, createdQuery.getClass());
    var boolQueryBuilder = (BoolQueryBuilder) createdQuery;

    assertEquals("1", boolQueryBuilder.minimumShouldMatch());
    assertEquals(2, boolQueryBuilder.should().size());
    assertEquals(NestedQueryBuilder.class, boolQueryBuilder.should().get(0).getClass());
    assertEquals(NestedQueryBuilder.class, boolQueryBuilder.should().get(1).getClass());

    var nested = (NestedQueryBuilder) boolQueryBuilder.should().get(0);

    //you cannot access boolQueryBuilder.path, so we do a hack
    assertTrue(nested.toString().contains("contact"));
    assertEquals(MatchQueryBuilder.class, nested.query().getClass());
    var matchQuery = (MatchQueryBuilder) nested.query();
    assertEquals("contact.organisationObject.default", matchQuery.fieldName());
    assertEquals("jody", matchQuery.value());

    var nested2 = (NestedQueryBuilder) boolQueryBuilder.should().get(1);

    //you cannot access boolQueryBuilder.path, so we do a hack
    assertTrue(nested2.toString().contains("contact"));
    assertEquals(MatchQueryBuilder.class, nested2.query().getClass());
    var matchQuery2 = (MatchQueryBuilder) nested2.query();
    assertEquals("contact.organisationObject.lang*", matchQuery2.fieldName());
    assertEquals("jody", matchQuery2.value());
  }

  //-------------------------------------------------------------------------------

  /**
   * simple query building for a <queryable>=<search value>
   *
   * @param queryablesService
   * @param pname
   * @param pvalue
   * @return
   */
  public Query buildQuery(QueryablesService queryablesService, String pname, String pvalue) {
    //setup QueryBuilder
    var queryBuilder = new QueryBuilder();
    queryBuilder.queryablesService = queryablesService;

    Map<String, String[]> paramMap = new LinkedHashMap<>();
    paramMap.put(pname, new String[]{pvalue});

    //setup Query
    var query = queryBuilder.buildFromRequest(
        "abc", null, null, null, null, null, null,
        null, null, null, paramMap);

    return query;
  }

}
