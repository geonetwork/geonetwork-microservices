package org.fao.geonet.index.converter;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.fao.geonet.index.model.gn.IndexRecord;

/**
 * See https://github.com/geonetwork/core-geonetwork/blob/master/schemas/iso19139/src/main/plugin/iso19139/formatter/jsonld/iso19139-to-jsonld.xsl
 */
public class JsonLdRecord {

  ObjectNode root = JsonNodeFactory.instance.objectNode();

  public JsonLdRecord() {
  }

  /**
   * Convert an index document into a JSON LD document.
   */
  public JsonLdRecord(IndexRecord record) {
    root.put("@context", "http://schema.org/");

    record.getResourceType().forEach(type -> {
      root.put("@type", type);
    });
    if (record.getResourceType().size() == 0) {
      root.put("@type", "schema:Dataset");
    }

    root.put("@id", record.getMetadataIdentifier());
    root.put("name", record.getResourceTitle().get("default"));
    root.put("description", record.getResourceAbstract().get("default"));
    root.put("dateCreated", record.getCreateDate());
    root.put("dateModified", record.getChangeDate());
  }

  public String toString() {
    return root.toPrettyString();
  }
}
