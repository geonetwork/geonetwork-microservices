/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OGCAPI link Template. https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/linkTemplate.yaml
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OgcApiLinkTemplate extends OgcApiLink {

  /**
   * Supplies a resolvable URI to a remote resource (or resource fragment). example:
   * http://data.example.com/buildings/(building-id}
   */
  private String uriTemplate;

  /**
   * The base URI to which the variable name can be appended to retrieve the definition of the
   * variable as a JSON Schema fragment. format: uri
   */
  private String varBase;

  /**
   * This object contains one key per substitution variable in the templated URL.  Each key defines
   * the schema of one substitution variable using a JSON Schema fragment and can thus include
   * things like the data type of the variable, enumerations, minimum values, maximum values, etc.
   */
  private Map<String, JsonProperty> variables;
}
