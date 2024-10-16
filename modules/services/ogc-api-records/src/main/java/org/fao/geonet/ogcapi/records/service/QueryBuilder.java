/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.service;


import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.ogcapi.records.controller.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for Query Processing.
 *
 * <p>Build a Query from a request
 *
 * <p>Handle the "extra parameters" (queryables)
 *
 * <p>Help Convert between OGCAPI Query and Elastic
 */
@Service
@Slf4j(topic = "org.fao.geonet.ogcapi.records")
public class QueryBuilder {

  @Autowired
  QueryablesService queryablesService;


  /**
   * builds a query from the request - cf. ItemApiController#collectionsCollectionIdItemsGet.
   *
   * @param collectionId see {@link Query#collectionId}
   * @param bbox         see {@link Query#bbox}
   * @param datetime     see {@link Query#datetime}
   * @param limit        see {@link Query#limit}
   * @param startindex   see {@link Query#startIndex}
   * @param type         see {@link Query#type}
   * @param q            see {@link Query#q}
   * @param ids          see {@link Query#ids}
   * @param externalids  see {@link Query#externalIds}
   * @param sortby       see {@link Query#sortBy}
   * @param parameterMap see {@link Query#propValues}
   * @return query object filled in
   */
  public Query buildFromRequest(String collectionId,
      List<BigDecimal> bbox,
      String datetime,
      Integer limit,
      Integer startindex,
      String type,
      List<String> q,
      List<String> ids,
      List<String> externalids,
      List<String> sortby,
      Map<String, String[]> parameterMap) {

    var result = new Query();

    result.setCollectionId(collectionId);

    if (bbox != null && bbox.size() > 0) {
      result.setBbox(bbox.stream().map(x -> x.doubleValue()).collect(Collectors.toList()));
    }

    result.setLimit(limit);
    result.setStartIndex(startindex);
    result.setDatetime(datetime);
    result.setType(type);
    result.setQ(q);
    result.setIds(ids);

    result.setExternalIds(externalids);
    result.setSortBy(sortby);

    injectExtraFromRequest(collectionId, result, parameterMap);

    return result;
  }


  /**
   * handle the "other" Queryables (cf Queryables).
   *
   * @param result       existing query to augment
   * @param parameterMap parameters from the request query string
   */
  private void injectExtraFromRequest(String collectionId, Query result,
      Map<String, String[]> parameterMap) {
    var queryables = queryablesService.buildQueryables(collectionId)
        .getProperties();

    result.setPropValues(new LinkedHashMap<>());

    //foreach key-value pair in the parameter map
    for (var param : parameterMap.entrySet()) {
      var queryable = queryables.get(param.getKey());
      if (queryable != null) {
        //we found a param (key-value) in the request that matches one of our queryables
        var values = param.getValue();
        if (values == null || values.length == 0) {
          continue;
        }
        result.getPropValues().put(param.getKey(), values[0]);
      }
    }
  }

}
