/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.fao.geonet.index.model.gn.Contact;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames.Codelists;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames.CommonField;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.geojson.GeoJsonReader;

/**
 * Index document to JSON-LD mapping.
 *
 * <p>Previous implementation https://github.com/geonetwork/core-geonetwork/blob/master/schemas/iso19139/src/main/plugin/iso19139/formatter/jsonld/iso19139-to-jsonld.xsl
 *
 * <p>Based on https://schema.org/Dataset
 *
 * <p>Tested with https://search.google.com/test/rich-results
 *
 * <p>TODO: Add support to translation https://bib.schema.org/workTranslation
 */
public class SchemaOrgConverter {

  public static Map<String, String> dateMapping = Map.ofEntries(
      new AbstractMap.SimpleEntry<>("creation", "dateCreated"),
      new AbstractMap.SimpleEntry<>("publication", "datePublished"),
      new AbstractMap.SimpleEntry<>("revision", "dateModified"),
      new AbstractMap.SimpleEntry<>("superseded", "expires")
  );
  public static Map<String, String> contactRoleMapping = Map.ofEntries(
      new AbstractMap.SimpleEntry<>("author", "author"),
      new AbstractMap.SimpleEntry<>("coAuthor", "author"),
      new AbstractMap.SimpleEntry<>("pointOfContact", "creator"),
      new AbstractMap.SimpleEntry<>("originator", "creator"),
      new AbstractMap.SimpleEntry<>("processor", "contributor"),
      new AbstractMap.SimpleEntry<>("contributor", "contributor"),
      new AbstractMap.SimpleEntry<>("resourceProvider", "provider"),
      new AbstractMap.SimpleEntry<>("distributor", "provider"),
      new AbstractMap.SimpleEntry<>("custodian", "maintainer"),
      new AbstractMap.SimpleEntry<>("owner", "copyrightHolder"),
      new AbstractMap.SimpleEntry<>("rightsHolder", "copyrightHolder"),
      new AbstractMap.SimpleEntry<>("user", "user"),
      new AbstractMap.SimpleEntry<>("collaborator", "user"),
      new AbstractMap.SimpleEntry<>("principalInvestigator", "sourceOrganization"),
      new AbstractMap.SimpleEntry<>("publisher", "publisher"),
      new AbstractMap.SimpleEntry<>("sponsor", "sponsor"),
      new AbstractMap.SimpleEntry<>("editor", "editor"),
      new AbstractMap.SimpleEntry<>("funder", "funder"),

      // No equivalent in ISO?
      new AbstractMap.SimpleEntry<>("accountablePerson", "accountablePerson"),
      new AbstractMap.SimpleEntry<>("producer", "producer"),
      new AbstractMap.SimpleEntry<>("translator", "translator")
  );
  public static Map<String, String> resourceTypeMapping = Map.ofEntries(
      new AbstractMap.SimpleEntry<>("dataset", "Dataset"),
      new AbstractMap.SimpleEntry<>("series", "Dataset"),
      new AbstractMap.SimpleEntry<>("service", "WebAPI"),
      new AbstractMap.SimpleEntry<>("application", "SoftwareApplication"),
      new AbstractMap.SimpleEntry<>("collectionHardware", "Thing"),
      new AbstractMap.SimpleEntry<>("nonGeographicDataset", "Dataset"),
      new AbstractMap.SimpleEntry<>("dimensionGroup", "TechArticle"),
      new AbstractMap.SimpleEntry<>("featureType", "Dataset"),
      new AbstractMap.SimpleEntry<>("model", "TechArticle"),
      new AbstractMap.SimpleEntry<>("tile", "Dataset"),
      new AbstractMap.SimpleEntry<>("fieldSession", "Project"),
      new AbstractMap.SimpleEntry<>("collectionSession", "Project")
  );
  private static ObjectMapper mapper = new ObjectMapper();

  /**
   * Convert an index document into a JSON LD document.
   */
  public static ObjectNode convert(IndexRecord record) {
    ObjectNode root = JsonNodeFactory.instance.objectNode();

    // ObjectNode context = root.putObject("@context");
    root.put("@context", "http://schema.org/");

    if (record.getResourceType().size() > 0) {
      record.getResourceType().forEach(type -> {
        root.put("@type",
            resourceTypeMapping.get(type) != null ? resourceTypeMapping.get(type) : type);
      });
    } else {
      root.put("@type", Types.Dataset.name());
    }

    root.put("@id", record.getMetadataIdentifier());
    root.put("name", record.getResourceTitle().get(CommonField.defaultText));
    // root.put("http://schema.org/name",
    // record.getResourceTitle().get(CommonField.defaultText));

    // url
    // URL of the item.
    // > DOI?

    // A secondary title of the CreativeWork.
    if (record.getResourceAltTitle().size() > 0) {
      ArrayNode array = root.putArray("alternateName");
      record.getResourceAltTitle().forEach(t -> {
        addOptional(array, null, t.get(CommonField.defaultText));
      });
    }

    // version
    // The version of the CreativeWork embodied by a specified resource.
    // identifier

    // The identifier property represents any kind of identifier
    // for any kind of Thing, such as ISBNs, GTIN codes, UUIDs etc.
    // Schema.org provides dedicated properties for representing many of
    // these, either as textual strings or as URL (URI) links.
    // See background notes for more details.
    if (record.getResourceIdentifier().size() > 0) {
      ArrayNode array = root.putArray("identifier");
      record.getResourceIdentifier().forEach(i -> {
        addOptional(array, null, i);
      });
    }

    // An abstract is a short description that summarizes a CreativeWork.
    root.put("description", record.getResourceAbstract().get(CommonField.defaultText));

    if (record.getOverview().size() > 0) {
      ArrayNode array = root.putArray("thumbnailUrl");
      record.getOverview().forEach(o -> {
        array.add(o.getUrl());
      });
    }

    record.getResourceDate().forEach(d -> {
      String type = dateMapping.get(d.getType());
      // TODO: Handle multiple date of same type?
      if (type != null) {
        addOptional(root, type, d.getDate());
      }
    });

    // The status of a creative work in terms of its stage in a lifecycle.
    // Example terms include Incomplete, Draft, Published, Obsolete.
    // Some organizations define a set of terms for the stages of
    // their publication lifecycle.
    addOptional(root, "creativeWorkStatus",
        record.getOtherProperties().get(Codelists.status));

    // Text that can be used to credit person(s) and/or organization(s)
    // associated with a published Creative Work.
    addOptional(root, "creditText",
        record.getResourceCredit());

    // Keywords or tags used to describe this content.
    // Multiple entries in a keywords list are typically delimited by commas.

    if (record.getTag().size() > 0) {
      ArrayNode array = root.putArray("keywords");
      record.getTag().forEach(k -> {
        addOptional(array, "keywords", k.get(CommonField.defaultText));
      });
    }

    // conditionsOfAccess
    // Conditions that affect the availability of, or method(s) of access to,
    // an item. Typically used for real world items such as an ArchiveComponent
    // held by an ArchiveOrganization.
    // This property is not suitable for use as a general Web access control
    // mechanism. It is expressed only in natural language.

    // license
    // A license document that applies to this content, typically indicated by URL.

    // acquireLicensePage
    // Indicates a page documenting how licenses can be purchased or
    // otherwise acquired, for the current item.

    // isAccessibleForFree
    // A boolean flag to signal that the item, event, or place is accessible for free.
    // Supersedes free.

    // distribution
    // A downloadable form of this dataset, at a specific location,
    // in a specific format.
    // https://schema.org/DataDownload
    if (record.getLinks().size() > 0) {
      ArrayNode array = root.putArray("distribution");
      record.getLinks().forEach(l -> {
        ObjectNode distribution =
            createThing(null, Types.DataDownload, root);
        // Actual bytes of the media object, for example the image file or video file.
        distribution.put("contentUrl", l.getUrl().get(CommonField.defaultText));
        addOptional(distribution, "name", l.getName());
        addOptional(distribution, "abstract", l.getDescription());
        addOptional(distribution, "encodingFormat", l.getProtocol());

        array.add(distribution);
      });
    }

    // encoding
    // A media object that encodes this CreativeWork.
    // This property is a synonym for associatedMedia. Supersedes encodings.
    // > Formats ?

    // encodingFormat
    // Media type typically expressed using a MIME format
    // (see IANA site and MDN reference) e.g. application/zip
    // for a SoftwareApplication binary, audio/mpeg for .mp3 etc.).
    // Supersedes fileFormat.
    if (record.getFormats().size() > 0) {
      ArrayNode array = root.putArray("encodingFormat");
      record.getFormats().forEach(l -> {
        addOptional(array, null, l);
      });
    }

    if (record.getResourceLanguage().size() > 0) {
      ArrayNode array = root.putArray("inLanguage");
      record.getResourceLanguage().forEach(l -> {
        addOptional(array, "inLanguage", l);
      });
    }

    // spatialCoverage
    // The spatialCoverage of a CreativeWork indicates the place(s)
    // which are the focus of the content. It is a subproperty of
    // contentLocation intended primarily for more technical and
    // detailed materials. For example with a Dataset, it indicates
    // areas that the dataset describes: a dataset of New York weather
    // would have spatialCoverage which was the place: the state of New York.
    if (record.getGeometries().size() > 0) {
      ObjectNode spatialCoverage = root.putObject("spatialCoverage");
      ArrayNode geo = spatialCoverage.putArray("geo");
      record.getGeometries().forEach(g -> {
        GeoJsonReader geoJsonReader = new GeoJsonReader();
        try {
          ObjectNode shape = createThing(null, Types.GeoShape, root);
          Geometry geometry = geoJsonReader.read(g);
          Envelope envelope = geometry.getEnvelopeInternal();
          // https://schema.org/GeoShape
          addOptional(shape, "box",
              String.format("%s %s %s %s",
                  envelope.getMinY(), envelope.getMinX(),
                  envelope.getMaxY(), envelope.getMaxX()));
          geo.add(shape);
        } catch (ParseException e) {
          e.printStackTrace();
        }
      });
    }

    // temporalCoverage
    // The temporalCoverage of a CreativeWork indicates the period
    // that the content applies to, i.e. that it describes, either
    // as a DateTime or as a textual string indicating a time period
    // in ISO 8601 time interval format. In the case of a Dataset
    // it will typically indicate the relevant time period in a
    // precise notation (e.g. for a 2011 census dataset, the year
    // 2011 would be written "2011/2012"). Other forms of content
    // e.g. ScholarlyArticle, Book, TVSeries or TVEpisode may
    // indicate their temporalCoverage in broader terms -
    // textually or via well-known URL. Written works such as books
    // may sometimes have precise temporal coverage too,
    // e.g. a work set in 1939 - 1945 can be indicated in ISO 8601
    // interval format format via "1939/1945".
    //
    //Open-ended date ranges can be written with ".." in place of
    // the end date. For example, "2015-11/.." indicates a range
    // beginning in November 2015 and with no specified final date.
    // This is tentative and might be updated in future when ISO 8601
    // is officially updated. Supersedes datasetTimeInterval.

    // The author of this content or rating. Please note that author is
    // special in that HTML 5 provides a special mechanism for
    // indicating authorship via the rel tag.
    // That is equivalent to this and may be used interchangeably.
    // > Author of the record or dataset ?
    if (record.getResourceTemporalExtentDateRange().size() > 0) {
      ArrayNode array = root.putArray("temporalCoverage");
      record.getResourceTemporalExtentDateRange().forEach(r -> {
        addOptional(array, null,
            String.format("%s/%s", r.getGte(), r.getLte()));
      });
    }

    if (record.getContactForResource().size() > 0) {
      record.getContactForResource()
          .stream()
          .collect(Collectors.groupingBy(Contact::getRole))
          .forEach((role, contacts) -> {
            String jsonRole = contactRoleMapping.get(role);
            if (jsonRole != null) {
              ArrayNode array = root.putArray(jsonRole);
              contacts.forEach(c -> {
                // https://schema.org/Organization
                ObjectNode organization =
                    createThing(null, Types.Organization, root);
                addOptional(organization, "name", c.getOrganisation());
                addOptional(organization, "address", c.getAddress());
                addOptional(organization, "email", c.getEmail());

                // https://schema.org/URL
                ObjectNode url = createThing("url", Types.Url, organization);
                addOptional(url, "url", c.getWebsite());

                addOptional(organization, "telephone", c.getPhone());

                // https://schema.org/ContactPoint
                ObjectNode contactPoint = createThing("contactPoint", Types.ContactPoint,
                    organization);
                addOptional(contactPoint, "name", c.getIndividual());
                addOptional(contactPoint, "description", c.getPosition());
                // A person or organization can have different contact points,
                // for different purposes. For example, a sales contact point,
                // a PR contact point and so on. This property is used to
                // specify the kind of contact point.
                addOptional(contactPoint, "contactType", c.getRole());

                // logo
                ObjectNode logo = createThing("logo", Types.ImageObject, organization);
                addOptional(logo, "contentUrl", c.getLogo());

                array.add(organization);
              });
            }
          });
    }

    // associatedMedia
    // A media object that encodes this CreativeWork.
    // This property is a synonym for encoding.
    // > cl_characterSet

    // measurementTechnique
    // A technique or technology used in a Dataset (or DataDownload, DataCatalog),
    // corresponding to the method used for measuring the corresponding variable
    // (s) (described using variableMeasured).
    // This is oriented towards scientific and scholarly dataset publication
    // but may have broader applicability; it is not intended as a full
    // representation of measurement, but rather as a high level summary
    // for dataset discovery.
    // > Sensor for imagery

    // variableMeasured
    // The variableMeasured property can indicate (repeated as necessary)
    // the variables that are measured in some dataset,
    // either described as text or as pairs of identifier and
    // description using PropertyValue.
    // > Could be keywords about parameters

    // The overall rating, based on a collection of reviews or ratings, of the item.
    // https://schema.org/AggregateRating
    if (record.getRating() != null && record.getRating() != 0) {
      ObjectNode rating =
          createThing("aggregateRating", Types.AggregateRating, root);
      addOptional(rating, "ratingValue", record.getRating());
    }

    // usageInfo
    //
    // comment
    // Comments, typically from users.
    // https://schema.org/Comment
    // feedbackCount
    if (record.getFeedbackCount() != null && record.getFeedbackCount() != 0) {
      addOptional(root, "feedbackCount", record.getFeedbackCount());
    }

    // commentCount
    // The number of comments this CreativeWork (e.g. Article, Question or Answer)
    // has received. This is most applicable to works published in Web sites
    // with commenting system; additional comments may exist elsewhere.

    // discussionUrl
    // A link to the page containing the comments of the CreativeWork.

    // hasPart
    // isPartOf
    // Indicates an item or CreativeWork that this item, or
    // CreativeWork (in some sense), is part of.
    // > parent
    // isBasedOn
    // > source
    // mentions
    // > DQ report

    // includedInDataCatalog
    // A data catalog which contains this dataset. Supersedes catalog, includedDataCatalog.
    // Inverse property: dataset
    // "includedInDataCatalog":[{
    // "url":"<xsl:value-of select="concat($baseUrl, 'search#', $catalogueName)"/>",
    // "name":"<xsl:value-of select="$catalogueName"/>"}],

    return root;
  }

  private static void addOptional(ContainerNode thing, String name, String value) {
    if (StringUtils.isNotEmpty(value)) {
      if (thing instanceof ObjectNode) {
        ((ObjectNode) thing).put(name, value);
      } else {
        ((ArrayNode) thing).add(value);
      }
    }
  }

  private static void addOptional(ContainerNode thing, String name, Integer value) {
    if (thing instanceof ObjectNode) {
      ((ObjectNode) thing).put(name, value);
    } else {
      ((ArrayNode) thing).add(value);
    }
  }

  private static void addOptional(ContainerNode thing, String name, Object value) {
    if (value instanceof HashMap && name.startsWith(Codelists.prefix)) {
      addOptional(thing, name, ((HashMap<?, ?>) value).get(CommonField.defaultText));
    }
  }

  private static ObjectNode createThing(String name, Types type,
      ObjectNode in) {
    ObjectNode thing;
    if (name == null) {
      thing = mapper.createObjectNode();
    } else {
      thing = in.putObject(name);
    }
    thing.put("@type", type.name());
    return thing;
  }

  public enum Types {
    Dataset,
    DataFeed,
    Organization,
    ContactPoint,
    Distribution,
    DataDownload,
    AggregateRating,
    ImageObject,
    GeoShape,
    Url;
  }
}
