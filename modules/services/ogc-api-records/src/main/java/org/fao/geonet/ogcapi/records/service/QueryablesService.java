package org.fao.geonet.ogcapi.records.service;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.ogcapi.records.model.JsonProperty;
import org.fao.geonet.ogcapi.records.model.JsonSchema;
import org.springframework.stereotype.Service;

/**
 * Basic Service to handle Queryables according to the OGCAPI spec.
 */
@Service
@Slf4j(topic = "org.fao.geonet.ogcapi.records")
public class QueryablesService {

  /**
   * build a schema based on collection.  It will be based on the underlying elastic index json.
   *
   * <p>NOTE: these are hard coded at the moment.
   *
   * @param collectionId which collection
   * @return schema based on collection
   */
  public JsonSchema buildQueryables(String collectionId) {
    var jsonSchema = new JsonSchema();

    Map<String, JsonProperty> properties = new LinkedHashMap<>();
    jsonSchema.setProperties(properties);
    addStandardProperties(properties);

    return jsonSchema;
  }

  /**
   * cf. https://docs.ogc.org/DRAFTS/20-004.html
   *
   * <p>The only mandatory one is "id".
   *
   * @param properties existing set of properties to add to.
   */
  public void addStandardProperties(Map<String, JsonProperty> properties) {

    JsonProperty p;
    //table 8
    p = new JsonProperty(JsonProperty.TypeString, "id",
        "A unique record identifier assigned by the server.");
    p.setxOgcRole("id");
    properties.put("id", p);

    p = new JsonProperty(JsonProperty.TypeString, "created",
        "The date this record was created in the server.");
    properties.put("created", p);

    p = new JsonProperty(JsonProperty.TypeString, "updated",
        "The most recent date on which the record was changed.");
    properties.put("updated", p);

    //conformsTo -- not in Elastic Index JSON

    p = new JsonProperty(JsonProperty.TypeString, "language",
        "The language used for textual values (i.e. titles, descriptions, etc.)"
            + " of this record.");
    properties.put("language", p);

    p = new JsonProperty(JsonProperty.TypeString, "languages",
        "The list of other languages in which this record is available.");
    properties.put("languages", p);

    //links -- not in Elastic Index JSON
    //linkTemplates -- not in Elastic Index JSON

    //table 9

    //unclear what this maps to in elastic
    p = new JsonProperty(JsonProperty.TypeString, "title",
        "The nature or genre of the resource described by this record.");
    properties.put("type", p);

    p = new JsonProperty(JsonProperty.TypeString, "title",
        "A human-readable name given to the resource described by this record.");
    properties.put("title", p);

    p = new JsonProperty(JsonProperty.TypeString, "description",
        "A free-text description of the resource described by this record.");
    properties.put("description", p);

    p = new JsonProperty(JsonProperty.TypeString, "geometry",
        "A spatial extent associated with the resource described by this record.");
    properties.put("geometry", p);

    p = new JsonProperty(JsonProperty.TypeString, "time",
        "A temporal extent associated with the resource described by this record.");
    properties.put("time", p);

    p = new JsonProperty(JsonProperty.TypeString, "keywords",
        "A list of free-form keywords or tags associated with the resource"
            + " described by this record.");
    properties.put("keywords", p);

    p = new JsonProperty(JsonProperty.TypeString, "themes",
        "A knowledge organization system used to classify the resource"
            + " described by this resource.");
    properties.put("themes", p);

    p = new JsonProperty(JsonProperty.TypeString, "contacts",
        "A list of contacts qualified by their role(s) in association to the record"
            + " or the resource described by this record.");
    properties.put("contacts", p);

    //resourceLanguages  -- not in Elastic Index JSON
    //externalIds  -- not in Elastic Index JSON
    //formats  -- not in Elastic Index JSON

    p = new JsonProperty(JsonProperty.TypeString, "license",
        "The legal provisions under which the resource described by this record"
            + " is made available.");
    properties.put("license", p);

    //rights -- not in Elastic Index JSON

  }

}
