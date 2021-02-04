/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.common.search.processor.impl;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.json.JsonGeneratorImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Copy the JSON document and trigger callback on hits.hits object.
 */
public class ResponseParser {

  protected static final JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());

  boolean copyJson;
  JsonGenerator jsonGenerator;
  BigInteger total;
  String totalRelation;
  Double took;

  /**
   * Add additional information to json document elements.
   */
  public void matchHits(
      JsonParser parser,
      Object generator,
      TreeFilter callback,
      boolean streamJson)
      throws Exception {
    this.copyJson = streamJson;
    //    if (generator instanceof JsonParser jsonGenerator) {
    if (this.copyJson) {
      jsonGenerator = (JsonGenerator) generator;
    }
    filterObjectInPath(parser, generator,
        (par, gen) ->
            filterArrayElements(par, gen, (par1, gen1) ->
                filterTree(parser, generator, callback)),
        "hits", "hits");
  }

  /**
   * Filter the elements of a json array.
   */
  private void filterArrayElements(JsonParser parser, Object generator,
      JsonFilter callback) throws Exception {
    if (parser.getCurrentToken() != JsonToken.START_ARRAY) {
      throw new RuntimeException("Expecting an array");
    }
    if (this.copyJson) {
      jsonGenerator.writeStartArray();
    }
    while (parser.nextToken() != JsonToken.END_ARRAY) {
      callback.apply(parser, generator);
    }
    if (this.copyJson) {
      jsonGenerator.writeEndArray();
    }
  }

  /**
   * Filters the elements in the json paths provided.
   */
  private void filterObjectInPath(JsonParser parser, Object generator,
      JsonFilter callback,
      String... path) throws Exception {
    if (parser.getCurrentToken() != JsonToken.START_OBJECT) {
      throw new RuntimeException("Expecting an object");
    }

    if (this.copyJson) {
      jsonGenerator.writeStartObject();
    }

    while (parser.nextToken() != JsonToken.END_OBJECT) {
      final String name = parser.getCurrentName();
      if (name.equals(path[0])) {
        if (this.copyJson) {
          jsonGenerator.writeFieldName(name);
        }
        parser.nextToken();
        String test = parser.getCurrentName();
        if (path.length == 1) {
          callback.apply(parser, generator);
        } else {
          final String[] sub = Arrays.copyOfRange(path, 1, path.length);
          filterObjectInPath(parser, generator, callback, sub);
        }
      } else {
        if (this.copyJson) {
          jsonGenerator.copyCurrentStructure(parser);
        } else {
          collectHeaderInfo(parser, name);
        }
      }
    }

    if (this.copyJson) {
      jsonGenerator.writeEndObject();
    }
  }

  private void collectHeaderInfo(JsonParser parser, String name) throws IOException {
    //  took: 13,
    //  timed_out: false,
    //  _shards: {
    //  total: 1,
    //    successful: 1,
    //    skipped: 0,
    //    failed: 0
    //  },
    //  hits: {
    //    total: {
    //      value: 54,
    //      relation: "eq"
    //    },
    if ("took".equals(name)) {
      parser.nextToken();
      this.took = parser.getDoubleValue();
    } else if ("total".equals(name)) {
      parser.nextToken(); // object
      parser.nextToken(); // value
      parser.nextToken();
      this.total = parser.getBigIntegerValue();
      parser.nextToken(); // relation
      parser.nextToken();
      this.totalRelation = parser.getValueAsString();
      parser.nextToken();
      parser.nextToken();
    } else {
      parser.skipChildren();
    }
  }

  private void filterTree(JsonParser parser, Object generator, TreeFilter callback)
      throws Exception {
    if (parser.getCurrentToken() != JsonToken.START_OBJECT) {
      throw new RuntimeException("Expecting an object");
    }
    final JsonNode tree = parser.readValueAsTree();
    callback.apply((ObjectNode) tree);
    if (this.copyJson) {
      jsonGenerator.writeTree(tree);
    }
  }

  private interface JsonFilter {

    void apply(JsonParser parser, Object generator) throws Exception;
  }

  public interface TreeFilter {

    void apply(ObjectNode doc) throws Exception;
  }
}
