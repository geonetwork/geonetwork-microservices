/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.searching.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Arrays;

public class JsonStreamUtils {

  public static final JsonFactory jsonFactory = new JsonFactory(new ObjectMapper());

  /**
   * Filter the elements of a json array.
   *
   * @param parser      Json parser.
   * @param generator   Json generator.
   * @param callback    Filter callback.
   * @throws Exception  Error.
   */
  public static void filterArrayElements(JsonParser parser, JsonGenerator generator,
      JsonFilter callback) throws Exception {
    if (parser.getCurrentToken() != JsonToken.START_ARRAY) {
      throw new RuntimeException("Expecting an array");
    }
    generator.writeStartArray();
    while (parser.nextToken() != JsonToken.END_ARRAY) {
      callback.apply(parser, generator);
    }
    generator.writeEndArray();
  }

  /**
   * Filters the elements in the json paths provided.
   *
   * @param parser      Json parser.
   * @param generator   Json generator.
   * @param callback    Filter callback.
   * @param path        Json paths to filter.
   * @throws Exception  Error.
   */
  public static void filterObjectInPath(JsonParser parser, JsonGenerator generator,
      JsonFilter callback,
      String... path) throws Exception {
    if (parser.getCurrentToken() != JsonToken.START_OBJECT) {
      throw new RuntimeException("Expecting an object");
    }
    generator.writeStartObject();
    while (parser.nextToken() != JsonToken.END_OBJECT) {
      final String name = parser.getCurrentName();
      if (name.equals(path[0])) {
        generator.writeFieldName(name);
        parser.nextToken();
        if (path.length == 1) {
          callback.apply(parser, generator);
        } else {
          final String[] sub = Arrays.copyOfRange(path, 1, path.length);
          filterObjectInPath(parser, generator, callback, sub);
        }
      } else {
        generator.copyCurrentStructure(parser);
      }
    }
    generator.writeEndObject();
  }

  /**
   * Add additional information to json document elements.
   *
   * @param parser      Json parser.
   * @param generator   Json generator.
   * @param callback    Filter callback.
   * @throws Exception  Error.
   */
  public static void addInfoToDocs(JsonParser parser, JsonGenerator generator, TreeFilter callback)
      throws Exception {
    JsonStreamUtils.filterObjectInPath(parser, generator,
        (par, gen) ->
            JsonStreamUtils.filterArrayElements(par, gen, (par1, gen1) ->
                filterTree(parser, generator, callback)),
        "hits", "hits");
  }

  private static void filterTree(JsonParser parser, JsonGenerator generator, TreeFilter callback)
      throws Exception {
    if (parser.getCurrentToken() != JsonToken.START_OBJECT) {
      throw new RuntimeException("Expecting an object");
    }
    final JsonNode tree = parser.readValueAsTree();
    callback.apply((ObjectNode) tree);
    generator.writeTree(tree);
  }

  public interface JsonFilter {

    void apply(JsonParser parser, JsonGenerator generator) throws Exception;
  }

  public interface TreeFilter {

    void apply(ObjectNode doc) throws Exception;
  }
}
