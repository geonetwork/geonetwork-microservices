/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.converter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.fao.geonet.ogcapi.records.model.GeoJsonPolygon;
import org.fao.geonet.ogcapi.records.model.OgcApiLink;
import org.fao.geonet.ogcapi.records.model.OgcApiLinkTemplate;
import org.fao.geonet.ogcapi.records.model.OgcApiTime;

/**
 * See OGCAPI spec - Table 8. (8.2.2. Core properties) Core properties related to the catalog
 * record.
 */
@Data
@SuperBuilder
@AllArgsConstructor
public class OgcApiGeoJsonRecord {

  /**
   * A unique record identifier assigned by the server.
   */
  private String id;

  /**
   * A spatial extent associated with the resource described by this record. Can be null if there is
   * no associated spatial extent.
   */
  @JsonInclude(Include.NON_EMPTY)
  private GeoJsonPolygon geometry;

  /**
   * The extensions/conformance classes used in this record.
   */
  @JsonInclude(Include.NON_EMPTY)
  private List<String> conformsTo;

  /**
   * A temporal extent associated with the resource described by this record. Can be null if there
   * is no associated temporal extent.
   */
  @JsonInclude(Include.NON_EMPTY)
  private OgcApiTime time;

  /**
   * A list of links related to this record.
   */
  @JsonProperty(value = "links")
  @JsonInclude(Include.NON_EMPTY)
  private List<OgcApiLink> links;

  /**
   * A list of link templates related to this record.
   */
  @JsonProperty(value = "linkTemplates")
  @JsonInclude(Include.NON_EMPTY)
  private List<OgcApiLinkTemplate> linkTemplates;

  private String type = "Feature";


  /**
   * Extra properties.  Can have any properties you want.  However, OGCAPI-Records defines;
   *
   * <p>properties.created  - The date this record was created in the server.
   *
   * <p>properties.updated  - The  most recent date on which the record was changed.
   *
   * <p>properties.language  - The language used for
   * textual values (i.e. titles, descriptions, etc.) of this record.
   *
   * <p>properties.languages  - The list of other languages in which this record is available.
   *
   * <p>properties.type  - The nature or genre of the resource described by this record.
   *
   * <p>properties.title  - A human-readable name given to the resource described by this record.
   *
   * <p>properties.description  - A free-text description of the resource described by this record.
   *
   * <p>properties.keywords  - A list of free-form keywords or tags associated with the resource
   * described by this record.
   *
   * <p>properties.themes  - A knowledge organization system used to classify the resource
   * described by this resource.
   *
   * <p>properties.resourceLanguages  - The list of languages in which the resource described
   * by this record can be retrieved.
   *
   * <p>properties.externalIds  - One or more identifiers, assigned by an
   * external entity, for the resource described by this record.
   *
   * <p>properties.formats  - A list of
   * available distributions for the resource described by this record.
   *
   * <p>properties.contacts  - A list of contacts qualified by their role(s) in association to
   * the record or the resource described by this record.
   *
   * <p>properties.license  - The legal provisions under which the resource
   * described by this record is made available. NOTE: special format (see spec)
   *
   * <p>properties.rights  - A statement that concerns all rights not addressed by the license
   * such as a copyright statement.
   */
  @JsonProperty(value = "properties")
  private Map<String, Object> properties;

  public OgcApiGeoJsonRecord() {
    properties = new LinkedHashMap<>();
  }

  /**
   * sets a named property in the #properties object.
   * @param propertyName which property to set?
   * @param value value of the property.
   */
  public void setProperty(String propertyName, Object value) {
    if (value != null) {
      properties.put(propertyName, value);
    }
  }
}
