/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.fao.geonet.ogcapi.records.controller.Query;
import org.junit.Before;
import org.junit.Test;

public class QueryBuilderTest {

  QueryBuilder queryBuilder;


  String collectionId;
  List<BigDecimal> bbox;
  String datetime;
  Integer limit;
  Integer startindex;
  String type;
  List<String> q;
  List<String> ids;
  List<String> externalids;
  List<String> sortby;
  Map<String, String[]> parameterMap;

  @Before
  public void setup() {
    queryBuilder = new QueryBuilder();
    queryBuilder.queryablesService = new QueryablesService();

    collectionId = "collectionId";
    bbox = Arrays.asList(new BigDecimal(0), new BigDecimal(0), new BigDecimal(100),
        new BigDecimal(100));
    datetime = "2024-10-22T21:10:03Z";
    limit = 100;
    startindex = 10;
    type = "type";
    q = Arrays.asList("abc", "def");
    ids = Arrays.asList("id1", "id2");
    externalids = Arrays.asList("ex-id1", "ex-id2");
    sortby = Arrays.asList("sort-p1", "sort-p2");
    parameterMap = new LinkedHashMap<>();
  }

  /**
   * just make sure that all the data is being copied to the Query.
   */
  @Test
  public void testSimple() {
    Query query = buildSampleQuery();

    assertEquals("collectionId", query.getCollectionId());
    assertIterableEquals(query.bbox,
        Arrays.asList(new Double(0), new Double(0),
            new Double(100), new Double(100)));
    assertEquals(query.datetime, "2024-10-22T21:10:03Z");
    assertEquals(query.limit, new Integer(100));
    assertEquals(query.startIndex, new Integer(10));
    assertEquals(query.type, "type");
    assertEquals(query.q, Arrays.asList("abc", "def"));
    assertEquals(query.ids, Arrays.asList("id1", "id2"));
    assertEquals(query.externalIds, Arrays.asList("ex-id1", "ex-id2"));
    assertEquals(query.sortBy, Arrays.asList("sort-p1", "sort-p2"));
    assertEquals(query.propValues, new LinkedHashMap<>());
  }

  /**
   * test with a good queryable (one in the queryables list)
   */
  @Test
  public void testGoodQueryable() {
    parameterMap.put("id", new String[]{"ID"});
    Query query = buildSampleQuery();

    assertEquals(1, query.propValues.size());
    assertTrue(query.propValues.containsKey("id"));
    assertEquals("ID", query.propValues.get("id"));
  }

  /**
   * test with a good queryable (one in the queryables list)
   */
  @Test
  public void testBadQueryable() {
    parameterMap.put("BAD-QUERYABLE", new String[]{"ID"});
    Query query = buildSampleQuery();

    assertEquals(0, query.propValues.size());
  }


  public Query buildSampleQuery() {
    return queryBuilder.buildFromRequest(
        collectionId,
        bbox,
        datetime,
        limit,
        startindex,
        type,
        q,
        ids,
        externalids,
        sortby,
        parameterMap
    );
  }
}
