/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.fao.geonet.ogcapi.records.model.GnElasticInfo;
import org.fao.geonet.ogcapi.records.model.GnElasticInfo.ElasticColumnFormat;
import org.fao.geonet.ogcapi.records.model.GnElasticInfo.ElasticColumnType;
import org.fao.geonet.ogcapi.records.model.JsonProperty;
import org.fao.geonet.ogcapi.records.model.JsonSchema;
import org.springframework.stereotype.Service;

/**
 * Basic Service to handle Queryables according to the OGCAPI spec.
 */
@Service
@Slf4j(topic = "org.fao.geonet.ogcapi.records")
public class QueryablesService {

  /** full schema from queryables.json*/
  public static JsonSchema fullJsonSchema;

  /** full schema from queryables.json with elastic removed*/
  public static JsonSchema truncatedJsonSchema;

  static {
      fullJsonSchema= readQueryablesJsonSchema();

      var js = readQueryablesJsonSchema();
      if (js != null && js.getProperties() !=null) {
        for (var prop : js.getProperties().values()) {
          prop.setxGnElasticPath(null);
        }
      }
    truncatedJsonSchema=js;
  }

  public static JsonSchema readQueryablesJsonSchema() {
    InputStream is = QueryablesService.class
        .getClassLoader().getResourceAsStream("queryables/queryables.json");

    try {
      String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      text = text.replaceAll("(?m)^//.*", "");

      var objectMapper = new ObjectMapper();
      var result = objectMapper.readValue(text, JsonSchema.class);
      return result;
    } catch (IOException e) {
      log.debug("problem reading in Queryables - is it mal-formed?",e);
    }

    return null;
  }

  /**
   * build a schema based on collection.  It will be based on the underlying elastic index json.
   *
   * <p>NOTE: these are hard coded at the moment.
   *
   * @param collectionId which collection
   * @return schema based on collection (without elastic details)
   */
  public JsonSchema buildQueryables(String collectionId) {
    return truncatedJsonSchema;
  }


  /**
   *  Full version of the queryables - including the elastic details.
   *
   * @param collectionId which collection
   * @return schema based on collection (WITH elastic details)
   */
  public JsonSchema getFullQueryables(String collectionId) {
    return fullJsonSchema;
  }

}
