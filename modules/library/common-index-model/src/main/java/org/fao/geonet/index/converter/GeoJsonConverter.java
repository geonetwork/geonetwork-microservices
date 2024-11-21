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
import org.apache.commons.lang.StringUtils;
import org.fao.geonet.index.JsonUtils;
import org.fao.geonet.index.model.geojson.Address;
import org.fao.geonet.index.model.geojson.Contact;
import org.fao.geonet.index.model.geojson.Geometry;
import org.fao.geonet.index.model.geojson.Link;
import org.fao.geonet.index.model.geojson.Link.LinkBuilder;
import org.fao.geonet.index.model.geojson.Record;
import org.fao.geonet.index.model.geojson.Record.RecordBuilder;
import org.fao.geonet.index.model.geojson.Role;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames.CommonField;
import org.fao.geonet.index.model.gn.ResourceDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Index document to GeoJSON mapping.
 */
@Component
public class GeoJsonConverter implements IGeoJsonConverter {

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
            record.getGeometriesAsJsonString().get(0),
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
      geojsonRecord.getProperties().put("updated", record.getChangeDate());
    }

    // record created
    if (record.getCreateDate() != null) {
      geojsonRecord.getProperties().put("created", record.getCreateDate());
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
    List<Contact> recordContacts = new ArrayList<>();

    record.getContact().stream().forEach(contact -> {

      Contact geojsonContact = Contact.builder()
          .email(contact.getEmail())
          .phone(contact.getPhone())
          .address(Address.builder().deliveryPoint(List.of(contact.getAddress())).build())
          .build();

      if (StringUtils.isNotEmpty(contact.getRole())) {
        org.fao.geonet.index.model.geojson.Role.RoleBuilder roleBuilder = Role.builder()
            .name(contact.getRole());

        geojsonContact.setRoles(List.of(roleBuilder.build()));
      }

      if (StringUtils.isNotEmpty(contact.getIndividual())) {
        geojsonContact.setName(contact.getIndividual());
        if (StringUtils.isNotEmpty((contact.getOrganisation().get("default")))) {
          geojsonContact.setOrganization(contact.getOrganisation().get("default"));
        }
      } else if (StringUtils.isNotEmpty((contact.getOrganisation().get("default")))) {
        geojsonContact.setName(contact.getOrganisation().get("default"));
      }

      recordContacts.add(geojsonContact);

    });

    // contacts
    geojsonRecord.getProperties().put("contacts", recordContacts);

    // type
    geojsonRecord.getProperties()
        .put("type", record.getResourceType().stream().collect(Collectors.joining()));

    // TODO: license, rights, themes, externalIds

    return geojsonRecord;
  }
}
