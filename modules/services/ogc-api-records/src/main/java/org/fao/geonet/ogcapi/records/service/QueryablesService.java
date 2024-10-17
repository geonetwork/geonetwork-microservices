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

  /**
   * build a schema based on collection.  It will be based on the underlying elastic index json.
   *
   * <p>NOTE: these are hard coded at the moment.
   *
   * @param collectionId which collection
   * @return schema based on collection
   */
  public JsonSchema buildQueryables(String collectionId) {

    InputStream is = getClass().getClassLoader().getResourceAsStream("queryables/queryables.json");

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
    p.getxGnElasticPath().add(new GnElasticInfo("uuid", ElasticColumnType.KEYWORD));
    properties.put("id", p);

    p = new JsonProperty(JsonProperty.TypeString, "created",
        "The date this record was created in the server.");
    p.getxGnElasticPath().add(
        new GnElasticInfo("createdDate", ElasticColumnType.DATE, ElasticColumnFormat.NORMAL));
    properties.put("created", p);

    p = new JsonProperty(JsonProperty.TypeString, "updated",
        "The most recent date on which the record was changed.");
    p.getxGnElasticPath().add(
        new GnElasticInfo("resourceDate.date", ElasticColumnType.DATE, ElasticColumnFormat.NORMAL));
    properties.put("updated", p);

    //conformsTo -- not in Elastic Index JSON

    p = new JsonProperty(JsonProperty.TypeString, "language",
        "The language used for textual values (i.e. titles, descriptions, etc.)"
            + " of this record.");
    p.getxGnElasticPath().add(
        new GnElasticInfo("mainLanguage", ElasticColumnType.KEYWORD));
    properties.put("language", p);

    p = new JsonProperty(JsonProperty.TypeString, "languages",
        "The list of other languages in which this record is available.");
    p.getxGnElasticPath().add(
        new GnElasticInfo("otherLanguage", ElasticColumnType.KEYWORD));
    properties.put("languages", p);

    //links -- not in Elastic Index JSON
    //linkTemplates -- not in Elastic Index JSON

    //table 9

    //unclear what this maps to in elastic
    p = new JsonProperty(JsonProperty.TypeString, "type",
        "The nature or genre of the resource described by this record.");
    p.getxGnElasticPath().add(
        new GnElasticInfo("resourceType", ElasticColumnType.KEYWORD));
    properties.put("type", p);

    p = new JsonProperty(JsonProperty.TypeString, "title",
        "A human-readable name given to the resource described by this record.");
    p.getxGnElasticPath().add(
        new GnElasticInfo("resourceTitleObject.default", ElasticColumnType.TEXT));
    p.getxGnElasticPath().add(
        new GnElasticInfo("resourceTitleObject.lang${lang3iso}", ElasticColumnType.TEXT));
    properties.put("title", p);

    p = new JsonProperty(JsonProperty.TypeString, "description",
        "A free-text description of the resource described by this record.");
    p.getxGnElasticPath().add(
        new GnElasticInfo("resourceAbstractObject.default", ElasticColumnType.TEXT));
    p.getxGnElasticPath().add(
        new GnElasticInfo("resourceAbstractObject.lang${lang3iso}", ElasticColumnType.TEXT));
    properties.put("description", p);

    p = new JsonProperty(JsonProperty.TypeString, "geometry",
        "A spatial extent associated with the resource described by this record.");
    p.getxGnElasticPath().add(
        new GnElasticInfo("geom", ElasticColumnType.GEO));
    properties.put("geometry", p);

    p = new JsonProperty(JsonProperty.TypeString, "time",
        "A temporal extent associated with the resource described by this record.");
    p.getxGnElasticPath().add(
        new GnElasticInfo("resourceTemporalDateRange", ElasticColumnType.DATERANGE));
    properties.put("time", p);

    p = new JsonProperty(JsonProperty.TypeString, "keywords",
        "A list of free-form keywords or tags associated with the resource"
            + " described by this record.");
    p.getxGnElasticPath().add(
        new GnElasticInfo("tag.default", ElasticColumnType.TEXT));
    p.getxGnElasticPath().add(
        new GnElasticInfo("tag.lang${lang3iso}", ElasticColumnType.TEXT));
    properties.put("keywords", p);

    p = new JsonProperty(JsonProperty.TypeString, "themes",
        "A knowledge organization system used to classify the resource"
            + " described by this resource.");
    // looks like this index is disabled
    p.getxGnElasticPath().add(
        new GnElasticInfo("allKeywords.*.keywords.default", ElasticColumnType.TEXT));
    p.getxGnElasticPath().add(
        new GnElasticInfo("allKeywords.*.keywords.lang${lang3iso}", ElasticColumnType.TEXT));
    properties.put("themes", p);

    p = new JsonProperty(JsonProperty.TypeString, "contacts",
        "A list of contacts qualified by their role(s) in association to the record"
            + " or the resource described by this record.");
    p.getxGnElasticPath().add(
        new GnElasticInfo("contact.organisationObject.default", ElasticColumnType.TEXT));
    p.getxGnElasticPath().add(
        new GnElasticInfo("contact.organisationObject.lang${lang3iso}", ElasticColumnType.TEXT));
    p.getxGnElasticPath().add(
        new GnElasticInfo("contact.address", ElasticColumnType.TEXT));
    p.getxGnElasticPath().add(
        new GnElasticInfo("contact.role", ElasticColumnType.TEXT));
    p.getxGnElasticPath().add(
        new GnElasticInfo("contact.email", ElasticColumnType.KEYWORD));
    p.getxGnElasticPath().add(
        new GnElasticInfo("contact.website", ElasticColumnType.TEXT));
    p.getxGnElasticPath().add(
        new GnElasticInfo("contact.individual", ElasticColumnType.TEXT));
    p.getxGnElasticPath().add(
        new GnElasticInfo("contact.position", ElasticColumnType.TEXT));
    p.getxGnElasticPath().add(
        new GnElasticInfo("contact.phone", ElasticColumnType.TEXT));
    properties.put("contacts", p);

    //resourceLanguages  -- not in Elastic Index JSON
    //externalIds  -- not in Elastic Index JSON
    //formats  -- not in Elastic Index JSON

    //license isn't really easy to search since the iso and ogcapi define it differently

    //rights -- not in Elastic Index JSON
  }

}
