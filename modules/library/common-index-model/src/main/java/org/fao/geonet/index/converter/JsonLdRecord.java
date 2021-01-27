package org.fao.geonet.index.converter;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang.StringUtils;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames.CommonField;

/**
 * See
 * https://github.com/geonetwork/core-geonetwork/blob/master/schemas/iso19139/src/main/plugin/iso19139/formatter/jsonld/iso19139-to-jsonld.xsl
 * https://schema.org/Dataset
 */
public class JsonLdRecord {

  ObjectNode root = JsonNodeFactory.instance.objectNode();

  public JsonLdRecord() {
  }

  /**
   * Convert an index document into a JSON LD document.
   *
   */
  public JsonLdRecord(IndexRecord record) {
    root.put("@context", "http://schema.org/");

    if (record.getResourceType().size() > 0) {
      record.getResourceType().forEach(type -> {
        root.put("@type", type);
      });
    } else {
      root.put("@type", "Dataset");
    }

    root.put("@id", record.getMetadataIdentifier());
    root.put("name", record.getResourceTitle().get(CommonField.defaultText));

    // An abstract is a short description that summarizes a CreativeWork.
    root.put("abstract", record.getResourceAbstract().get(CommonField.defaultText));

    // alternativeHeadline
    // A secondary title of the CreativeWork.


    root.put("dateCreated", record.getCreateDate());
    root.put("dateModified", record.getChangeDate());

    // conditionsOfAccess
    // Conditions that affect the availability of, or method(s) of access to,
    // an item. Typically used for real world items such as an ArchiveComponent
    // held by an ArchiveOrganization.
    // This property is not suitable for use as a general Web access control
    // mechanism. It is expressed only in natural language.

    // acquireLicensePage
    // Indicates a page documenting how licenses can be purchased or
    // otherwise acquired, for the current item.

    // distribution
    // A downloadable form of this dataset, at a specific location,
    // in a specific format.
    // https://schema.org/DataDownload
    record.getLinks().forEach(l -> {
      ObjectNode distribution = root.putObject("distribution");
      distribution.put("@type", "DataDownload");
      // Actual bytes of the media object, for example the image file or video file.
      distribution.put("url", l.getUrl());
      if (StringUtils.isNotEmpty(l.getName())) {
        distribution.put("name", l.getName());
      }
      if (StringUtils.isNotEmpty(l.getDescription())) {
        distribution.put("abstract", l.getDescription());
      }
    });

    // associatedMedia
    // A media object that encodes this CreativeWork.
    // This property is a synonym for encoding.

    // The overall rating, based on a collection of reviews or ratings, of the item.
    // https://schema.org/AggregateRating
    if (record.getRating() != null) {
      ObjectNode rating = root.putObject("aggregateRating");
      rating.put("@type", "AggregateRating");
      rating.put("ratingValue", record.getRating());
    }

    // comment
    // Comments, typically from users.
    // https://schema.org/Comment

    // commentCount
    // The number of comments this CreativeWork (e.g. Article, Question or Answer)
    // has received. This is most applicable to works published in Web sites
    // with commenting system; additional comments may exist elsewhere.

  }

  public String toString() {
    return root.toPrettyString();
  }
}
