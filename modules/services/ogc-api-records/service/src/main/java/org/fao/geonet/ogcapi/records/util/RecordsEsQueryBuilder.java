package org.fao.geonet.ogcapi.records.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.fao.geonet.ogcapi.records.OgcApiConfiguration;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@Component
@ConstructorBinding
public class RecordsEsQueryBuilder {

  private static OgcApiConfiguration configuration;

  private static List<String> defaultSources = Arrays.asList("resourceTitle", "resourceAbstract",
      "resourceType", "uuid", "schema", "link", "allKeywords",
      "contactForResource", "cl_status", "edit", "id");

  public RecordsEsQueryBuilder(OgcApiConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * Creates a ElasticSearch query for a single record.
   *
   * @param recordId         Record uuid.
   * @param collectionFilter Filter to select the record in a collection scope.
   * @param includes         List of fields to return (null, retuns all).
   * @return ElasticSearch query.
   */
  public static String buildQuerySingleRecord(String recordId, String collectionFilter,
      List<String> includes) {

    if (includes == null) {
      return String.format("{\"from\": %d, \"size\": %d, "
              + "\"query\": {\"query_string\": "
              + "{\"query\": \"+_id:%s %s +isTemplate:n\"}}}",
          0, 1, recordId, collectionFilter);
    } else {
      return String.format("{\"from\": %d, \"size\": %d, "
              + "\"query\": {\"query_string\": "
              + "{\"query\": \"+_id:%s %s +isTemplate:n\"}}, "
              + "\"_source\": {\"includes\": [" + includes.stream().collect(
          Collectors.joining("\",\"", "\"", "\"")) + "]}}",
          0, 1, recordId, collectionFilter);

    }
  }

  /**
   * Creates a ElasticSearch query from Records API parameters.
   */
  public static String buildQuery(List<BigDecimal> bbox, Integer startIndex, Integer limit,
      String collectionFilter, List<String> sortBy) {
    String geoFilter = "";

    if (bbox != null) {
      geoFilter = String.format(", {\"geo_shape\": {\"geom\": {\n"
          + "                \"shape\": {\n"
          + "                    \"type\": \"envelope\",\n"
          + "                    \"coordinates\": [\n"
          + "                        [\n"
          + "                            %f,\n"
          + "                            %f\n"
          + "                        ],\n"
          + "                        [\n"
          + "                            %f,\n"
          + "                            %f\n"
          + "                        ]\n"
          + "                    ]\n"
          + "                },\n"
          + "                \"relation\": \"intersects\"\n"
          + "            }}}", bbox.get(0), bbox.get(1), bbox.get(2), bbox.get(3));
    }

    String sortByValue = "\"_score\"";

    if (sortBy != null) {
      List<String> sortByList = new ArrayList<>();
      sortBy.forEach(s -> {
        String[] sortByTokens = s.split(":");

        if (sortByTokens.length == 2) {
          sortByList.add(String.format("{\"%s\": \"%s\"}", sortByTokens[0], sortByTokens[1]));
        }
      });

      sortByValue = String.join(",", sortByList);
    }

    Set<String> sources = new HashSet(defaultSources);
    sources.addAll(configuration.getSources());

    return String.format("{\"from\": %d, \"size\": %d, "
            + "\"_source\": [%s],"
            + "\"sort\": [%s],"
            + "\"query\": {\"query_string\": "
            + "{\"query\": \"%s +isTemplate:n\"}} %s} ",
        startIndex, limit,
        sources
            .stream()
            .collect(Collectors.joining("\",\"", "\"", "\"")),
        sortByValue, collectionFilter, geoFilter);
  }
}
