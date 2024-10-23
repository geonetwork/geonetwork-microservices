/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.service;

import static org.fao.geonet.ogcapi.records.model.GnElasticInfo.ElasticColumnType.DATE;
import static org.fao.geonet.ogcapi.records.model.GnElasticInfo.ElasticColumnType.DATERANGE;
import static org.fao.geonet.ogcapi.records.model.GnElasticInfo.ElasticColumnType.GEO;

import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.geometry.Rectangle;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.fao.geonet.ogcapi.records.controller.Query;
import org.fao.geonet.ogcapi.records.model.GnElasticInfo;
import org.fao.geonet.ogcapi.records.model.GnElasticInfo.ElasticColumnType;
import org.fao.geonet.ogcapi.records.model.GnElasticInfo.ElasticQueryType;
import org.fao.geonet.ogcapi.records.model.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This helps to build the "extra" (&property=value) queryables in the OGCAPI search to an Elastic
 * Index query.
 *
 *
 * <p>see the documentation on "queryables.json" with goes into more depth.
 *
 * <p>This class uses the metadata in the "queryables.json" to determine how to make the elastic
 * query.
 */
@Service
@Slf4j(topic = "org.fao.geonet.ogcapi.records")
public class QueryToElastic {

  @Autowired
  QueryablesService queryablesService;

  /**
   * Given an already setup SearchSourceBuilder, add more queries to it
   * for any of the request &param=search-Text.
   * WHERE: param is a queryable (cf queryables.json).
   *
   * @param sourceBuilder mostly configured elastic query.
   * @param query information from the search request.
   */
  public void augmentWithQueryables(SearchSourceBuilder sourceBuilder,
      Query query) {
    var mainQuery = sourceBuilder.query();
    if (query.getPropValues() == null || query.getPropValues().isEmpty()) {
      return;
    }

    if (!(mainQuery instanceof BoolQueryBuilder)) {
      //need to handle this case - but dont think it can happen...
      throw new RuntimeException("ugg");
    }
    var boolQuery = (BoolQueryBuilder) mainQuery;

    var jsonSchema = queryablesService.getFullQueryables(query.getCollectionId());

    for (var prop : query.getPropValues().entrySet()) {
      var jsonProp = jsonSchema.getProperties().get(prop.getKey());
      var searchVal = prop.getValue();
      var q = create(jsonProp, searchVal, "*");
      if (q != null) {
        boolQuery.must(q);
      }
    }
  }

  /**
   * Given a JsonProperty, construct the appropriate Elastic Query.
   * Handles Geo, Nested, Time, and text searches.
   *
   * @param jsonProperty property to process.
   * @param userSearchTerm what user is searching for
   * @param lang3iso what language?
   * @return QueryBuilder for this particular queryable.
   */
  public QueryBuilder create(JsonProperty jsonProperty, String userSearchTerm, String lang3iso) {

    var isGeo = jsonProperty.getxGnElasticPath().stream()
        .anyMatch(x -> x.getElasticColumnType() == GEO);

    var isNested = jsonProperty.getxGnElasticPath().stream()
        .anyMatch(x -> x.getElasticQueryType() == ElasticQueryType.NESTED);

    var isTime = (jsonProperty.getxGnElasticPath().get(0).getElasticColumnType() == DATE)
        || (jsonProperty.getxGnElasticPath().get(0).getElasticColumnType() == DATERANGE);

    if (isTime) {
      return createVsDate(jsonProperty.getxGnElasticPath().get(0), userSearchTerm, lang3iso);
    }

    if (isGeo) {
      return createGeo(jsonProperty.getxGnElasticPath().get(0), userSearchTerm, lang3iso);
    }

    if (isNested) {
      return createNested(jsonProperty.getxGnElasticPath(), userSearchTerm, lang3iso);
    }

    return createMulti(jsonProperty.getxGnElasticPath(), userSearchTerm, lang3iso);
  }

  private QueryBuilder create(GnElasticInfo gnElasticPath, String userSearchTerm, String lang3iso) {
    if (gnElasticPath.getElasticColumnType() == ElasticColumnType.TEXT
        & StringUtils.countMatches(gnElasticPath.getElasticPath(), ".") > 2) {
      return createNested(gnElasticPath, userSearchTerm, lang3iso);
    }
    if (gnElasticPath.getElasticColumnType() == ElasticColumnType.TEXT
        & StringUtils.countMatches(gnElasticPath.getElasticPath(), ".") <= 2) {
      return createMatch(gnElasticPath, userSearchTerm, lang3iso);
    }
    if (gnElasticPath.getElasticColumnType() == ElasticColumnType.KEYWORD) {
      return createTerm(gnElasticPath, userSearchTerm, lang3iso);
    }
    return null;
  }

  /**
   * create a date range query.
   * @param gnElasticInfo column (singular) you are querying
   * @param userSearchTerm what searching for?  (this will be parsed to an interval)
   * @param lang3iso language
   * @return QueryBuilder with a data-based range query.
   */
  private QueryBuilder createVsDate(GnElasticInfo gnElasticInfo, String userSearchTerm,
      String lang3iso) {
    var result = QueryBuilders.rangeQuery(gnElasticInfo.getElasticPath());

    processDateRequest(result, userSearchTerm);

    return result;
  }


  /**
   * Format.
   *
   * <p>interval-bounded  = date-time "/" date-time interval-half-bounded-start = [".."] "/"
   * date-time interval-half-bounded-end   = date-time "/" [".."] interval       =
   *      interval-closed / interval-half-bounded-start / interval-half-bounded-end
   *  datetime = date-time / interval
   *
   * <p>The syntax of date-time is specified by RFC 3339, 5.6.
   * https://www.rfc-editor.org/rfc/rfc3339.html#section-5.6
   *
   * @param result RangeQueryBuilder to update with start/end (might only have start or end if "..")
   * @param userSearchTerm date or interval to parse.
   */
  private void processDateRequest(RangeQueryBuilder result, String userSearchTerm) {
    if (!userSearchTerm.contains("/")) {
      //its a single date (request) vs a date (elastic index)
      result.relation(ShapeRelation.INTERSECTS.getRelationName());
      result.gte(userSearchTerm);
      result.lte(userSearchTerm);
      return;
    }
    //interval
    var dateParts = userSearchTerm.split("/");
    if (dateParts.length != 2) {
      return; //error!
    }

    result.relation(ShapeRelation.INTERSECTS.getRelationName());
    if (!dateParts[0].equals("..")) {
      result.gte(dateParts[0]);
    }
    if (!dateParts[1].equals("..")) {
      result.lte(dateParts[1]);
    }
  }

  private QueryBuilder createGeo(GnElasticInfo gnElasticInfo, String userSearchTerm,
      String lang3iso) {

    var nums = userSearchTerm.split(",");
    if (nums.length != 4) {
      return null;
    }

    Rectangle rectangle = new Rectangle(
        Double.parseDouble(nums[0]),
        Double.parseDouble(nums[2]),
        Double.parseDouble(nums[3]),
        Double.parseDouble(nums[1]));

    try {
      var geoQuery = QueryBuilders
          .geoShapeQuery(gnElasticInfo.getElasticPath(), rectangle)
          .relation(ShapeRelation.getRelationByName("intersects"));
      return geoQuery;
    } catch (IOException e) {
      log.debug("problem constructing geoQuery - ignoring it!",e);
    }
    return null;
  }

  /**
   * convert a JsonProperty to an elastic QUERY.
   *
   * @param jsonProperty property
   * @return String representing the needed elastic query (or null if not possible)
   */
  public QueryBuilder convert(JsonProperty jsonProperty, String userSearchTerm, String lang3iso) {
    var gnElasticPath = jsonProperty.getxGnElasticPath();
    if (gnElasticPath == null || gnElasticPath.size() == 0) {
      return null;
    }
    if (gnElasticPath.size() > 1) {
      return createOR(gnElasticPath, userSearchTerm, lang3iso);
    }
    return create(gnElasticPath.get(0), userSearchTerm, lang3iso);
  }

  /**
   * create an elastic nested query.  The main path will be the 1st path part of the full path.
   * i.e. "contacts.phone" -> main path will be "contacts".
   *
   * @param columns what columns are we searching in.
   * @param userSearchTerm what is the userer searching for
   * @param lang3iso language
   * @return QueryBuilder (could be a nested, or an OR ("bool" "should" query) with multiple nested)
   */
  public QueryBuilder createNested(List<GnElasticInfo> columns,
      String userSearchTerm,
      String lang3iso) {

    if (columns.size() == 1) {
      return createNested(columns.get(0), userSearchTerm, lang3iso);
    }

    var result = QueryBuilders.boolQuery();

    for (var gnElasticInfo : columns) {
      var subQ = createNested(gnElasticInfo, userSearchTerm, lang3iso);
      result.should(subQ);
    }

    result.minimumShouldMatch(1);

    return result;
  }

  private QueryBuilder createNested(GnElasticInfo gnElasticPath,
      String userSearchTerm,
      String lang3iso) {
    userSearchTerm = userSearchTerm.replaceAll("\"", "");
    var path = gnElasticPath.getElasticPath();
    path = path.replace("${lang3iso}", lang3iso);
    var firstPartPath = path.substring(0, path.indexOf("."));

    var matchQueryBuilder = QueryBuilders.matchQuery(path, userSearchTerm);

    NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery(firstPartPath,
        matchQueryBuilder, ScoreMode.Max);
    nestedQueryBuilder.ignoreUnmapped(true);

    return nestedQueryBuilder;
  }


  /**
   * Create a match_multi elastic query for the given columns.
   * @param columns which columns?  (can contain "*")
   * @param userSearchTerm what text searching for?
   * @param lang3iso language to inject as "$P{iso3lang}
   * @return QueryBuilder with a match_multi
   */
  public QueryBuilder createMulti(List<GnElasticInfo> columns,
      String userSearchTerm,
      String lang3iso) {
    userSearchTerm = userSearchTerm.replaceAll("\"", "");

    var paths = columns.stream()
        .map(x -> x.getElasticPath())
        .map(x -> x.replace("${lang3iso}", lang3iso))
        .toArray(String[]::new);

    var result = QueryBuilders.multiMatchQuery(userSearchTerm, paths);
    //try to match for minor typos
    result.fuzziness(Fuzziness.AUTO);
    result.fuzzyTranspositions(true);
    result.lenient(true);
    result.minimumShouldMatch("1");
    result.operator(Operator.AND);  //capital of canada -> capital AND of AND canada
    return result;
  }




  private QueryBuilder createMatch(GnElasticInfo gnElasticPath, String userSearchTerm,
      String lang3iso) {
    userSearchTerm = userSearchTerm.replaceAll("\"", "");
    var path = gnElasticPath.getElasticPath();
    path = path.replace("${lang3iso}", lang3iso);

    if (path.contains("*")) {
      var queryBuilder = QueryBuilders.multiMatchQuery(userSearchTerm, path);
      return queryBuilder;
    }

    var queryBuilder = QueryBuilders.matchQuery(path, userSearchTerm);
    return queryBuilder;

  }

  private QueryBuilder createTerm(GnElasticInfo gnElasticPath, String userSearchTerm,
      String lang3iso) {
    userSearchTerm = userSearchTerm.replaceAll("\"", "");
    var path = gnElasticPath.getElasticPath();
    path = path.replace("${lang3iso}", lang3iso);
    var queryBuilder = QueryBuilders.termsQuery(path, userSearchTerm);
    return queryBuilder;
  }


  /**
   * elastic "SHOULD" is an "OR".
   *
   * @param gnElasticPaths set of queries to OR ("should") together
   * @return String representing the needed elastic query (or null if not possible)
   */
  private QueryBuilder createOR(List<GnElasticInfo> gnElasticPaths,
      String userSearchTerm,
      String lang3iso) {

    var boolQuerybuilder = QueryBuilders.boolQuery();
    boolQuerybuilder.minimumShouldMatch(1);

    for (var gnElasticInfo : gnElasticPaths) {
      var orItem = create(gnElasticInfo, userSearchTerm, lang3iso);
      if (orItem != null) {
        boolQuerybuilder.should(orItem);
      }
    }
    return boolQuerybuilder;
  }

}
