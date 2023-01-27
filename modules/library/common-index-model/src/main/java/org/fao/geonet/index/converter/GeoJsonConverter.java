/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.converter;

import static org.fao.geonet.index.model.gn.IndexRecordFieldNames.CommonField.defaultText;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.fao.geonet.index.JsonUtils;
import org.fao.geonet.index.model.geojson.Geometry;
import org.fao.geonet.index.model.geojson.Link;
import org.fao.geonet.index.model.geojson.Link.LinkBuilder;
import org.fao.geonet.index.model.geojson.Record;
import org.fao.geonet.index.model.geojson.Record.RecordBuilder;
import org.fao.geonet.index.model.gn.Contact;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames.CommonField;
import org.fao.geonet.index.model.gn.ResourceDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Index document to GeoJSON mapping.
 */
@Component
public class GeoJsonConverter {

  @Autowired
  FormatterConfiguration formatterConfiguration;

  /**
   * Convert an index document into a DCAT object.
   */
  public Record convert(IndexRecord record) {
    RecordBuilder recordBuilder = Record.builder()
        .id(record.getMetadataIdentifier())
        .type("Feature")
        .properties(new HashMap<>());

    List<Link> recordLinks = new ArrayList<>();

    if (!record.getGeometries().isEmpty()) {
      try {
        ObjectMapper objectMapper = JsonUtils.getObjectMapper();

        recordBuilder.geometry(objectMapper.readValue(
            record.getGeometries().get(0),
            Geometry.class));
      } catch (Exception ex) {
        // TODO: Process the exception
        ex.printStackTrace();
      }

    }

    record.getLinks().stream().forEach(link -> {

      LinkBuilder linkBuilder = Link.builder()
          .href(link.getUrl().get(CommonField.defaultText))
          .rel("item")
          .title(link.getName().get(CommonField.defaultText))
          .type(link.getProtocol());

      recordLinks.add(linkBuilder.build());

    });

    recordBuilder.links(recordLinks);

    Record geojsonRecord = recordBuilder.build();

    // record updated
    if (record.getChangeDate() != null) {
      geojsonRecord.getProperties().put("recordUpdated", record.getChangeDate());
    }

    // record created
    if (record.getCreateDate() != null) {
      geojsonRecord.getProperties().put("recordCreated", record.getCreateDate());
    }

    // title
    geojsonRecord.getProperties().put("title", record.getResourceTitle().get(defaultText));

    // description
    geojsonRecord.getProperties().put("description", record.getResourceAbstract().get(defaultText));

    // language
    geojsonRecord.getProperties().put("language", record.getMainLanguage());

    // resource dates
    for (ResourceDate resourceDate : record.getResourceDate()) {
      if (resourceDate.getType().equals("revision")) {
        geojsonRecord.getProperties().put("revision", resourceDate.getDate());
      } else if (resourceDate.getType().equals("created")) {
        geojsonRecord.getProperties().put("created", resourceDate.getDate());
      }
    }

    // freetext keywords
    List<String> keywords = new ArrayList<>();

    record.getTag().forEach(tag -> {
      keywords.add(tag.get(defaultText));
    });

    geojsonRecord.getProperties().put("keywords", keywords);

    // formats
    if (!record.getFormats().isEmpty()) {
      geojsonRecord.getProperties().put("formats", record.getFormats());
    }

    // providers
    List<String> providers = new ArrayList<>();
    for (Contact contact : record.getContact()) {
      providers.add(String.format("%s, %s, %s", contact.getIndividual(), contact.getEmail(),
          contact.getOrganisation()));
    }

    geojsonRecord.getProperties().put("providers", providers);

    // type
    geojsonRecord.getProperties()
        .put("type", record.getResourceType().stream().collect(Collectors.joining()));

    // TODO: license, rights, themes, externalIds

    return geojsonRecord;
  }
}
