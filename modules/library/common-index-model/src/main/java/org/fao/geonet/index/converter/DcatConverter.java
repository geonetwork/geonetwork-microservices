/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.converter;

import static org.fao.geonet.index.model.dcat2.Dataset.ACCRUAL_PERIODICITY_TO_ISO;
import static org.fao.geonet.index.model.dcat2.Dataset.ACCRUAL_PERIODICITY_URI_PREFIX;
import static org.fao.geonet.index.model.gn.IndexRecordFieldNames.Codelists.topic;
import static org.fao.geonet.index.model.gn.IndexRecordFieldNames.CommonField.defaultText;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.fao.geonet.index.model.dcat2.CatalogRecord;
import org.fao.geonet.index.model.dcat2.Dataset;
import org.fao.geonet.index.model.dcat2.Dataset.DatasetBuilder;
import org.fao.geonet.index.model.dcat2.DcatActivity;
import org.fao.geonet.index.model.dcat2.DcatContactPoint;
import org.fao.geonet.index.model.dcat2.DcatDistribution;
import org.fao.geonet.index.model.dcat2.DcatDistribution.DcatDistributionBuilder;
import org.fao.geonet.index.model.dcat2.DcatDistributionContainer;
import org.fao.geonet.index.model.dcat2.DcatDocument;
import org.fao.geonet.index.model.dcat2.DctLocation;
import org.fao.geonet.index.model.dcat2.DctPeriodOfTime;
import org.fao.geonet.index.model.dcat2.DctPeriodOfTime.DctPeriodOfTimeBuilder;
import org.fao.geonet.index.model.dcat2.DctSpatial;
import org.fao.geonet.index.model.dcat2.DctTemporal;
import org.fao.geonet.index.model.dcat2.FoafDocument;
import org.fao.geonet.index.model.dcat2.ProvActivity;
import org.fao.geonet.index.model.dcat2.ProvGenerated;
import org.fao.geonet.index.model.dcat2.ProvHadPlan;
import org.fao.geonet.index.model.dcat2.ProvQualifiedAssociation;
import org.fao.geonet.index.model.dcat2.Provenance;
import org.fao.geonet.index.model.dcat2.ProvenanceStatement;
import org.fao.geonet.index.model.dcat2.RdfResource;
import org.fao.geonet.index.model.dcat2.ResourceContainer;
import org.fao.geonet.index.model.dcat2.SkosConcept;
import org.fao.geonet.index.model.dcat2.Subject;
import org.fao.geonet.index.model.dcat2.VcardContact;
import org.fao.geonet.index.model.gn.Codelist;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames.Codelists;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames.CommonField;
import org.fao.geonet.index.model.gn.ResourceIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Index document to DCAT mapping.
 */
@Component
public class DcatConverter {

  @Autowired
  FormatterConfiguration formatterConfiguration;

  private static final Map<String, String> RESSOURCE_TYPE_MAPPING = Map.ofEntries(
      new AbstractMap.SimpleEntry<>("dataset", "Dataset"),
      new AbstractMap.SimpleEntry<>("series", "Dataset"),
      new AbstractMap.SimpleEntry<>("service", "DataService"),
      new AbstractMap.SimpleEntry<>("application", "DataService"),
      new AbstractMap.SimpleEntry<>("nonGeographicDataset", "Dataset"),
      new AbstractMap.SimpleEntry<>("featureType", "Dataset"),
      new AbstractMap.SimpleEntry<>("tile", "Dataset")
  );

  private static final Map<String, String> INSPIRE_DEGREE_OF_CONFORMITY = Map.of(
      "true", "conformant",
      "false", "notConformant",
      "", "notEvaluated"
  );

  @Value("${gn.language.default:en}")
  private String defaultLanguage;

  private ObjectMapper mapper = new ObjectMapper();


  /**
   * Convert an index document into a DCAT object.
   */
  public CatalogRecord convert(JsonNode doc) {
    CatalogRecord catalogRecord = null;
    Dataset dcatDataset = null;
    try {
      IndexRecord record = new ObjectMapper()
          .enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
          .readValue(doc.get(IndexRecordFieldNames.source).toString(), IndexRecord.class);

      String recordIdentifier = record.getMetadataIdentifier();
      String recordUri = formatterConfiguration.buildLandingPageLink(
              record.getMetadataIdentifier());
      Optional<ResourceIdentifier> resourceIdentifier =
          record.getResourceIdentifier().stream().filter(Objects::nonNull).findFirst();

      // TODO: Define strategy to build IRI
      final String resourceIdentifierUri = resourceIdentifier.isPresent()
          ? "local:" + resourceIdentifier.get().getCode()
          : null;

      String language = record.getMainLanguage() == null
          ? defaultLanguage : record.getMainLanguage();
      String languageUpperCase = language.toUpperCase();
      // TODO: Need language mapper
      String iso2letterLanguage = language.substring(0, 2);

      List<String> resourceLanguage = record.getResourceLanguage();

      List<String> resourceType = record.getResourceType();
      boolean isInspireResource = resourceType.contains("dataset")
          || resourceType.contains("series")
          || resourceType.contains("service");

      // TODO: Add multilingual support
      // TODO .resource("https://creativecommons.org/publicdomain/zero/1.0/deed")


      DatasetBuilder datasetBuilder = Dataset.builder()
          .identifier(record.getResourceIdentifier().stream()
              .map(c -> c.getCode()).collect(
              Collectors.toList()))
          .title(listOfNullable(record.getResourceTitle().get(defaultText)))
          .description(listOfNullable(record.getResourceAbstract().get(defaultText)))
          .landingPage(listOfNullable(DcatDocument.builder()
              .foafDocument(FoafDocument.builder()
                  .about(formatterConfiguration.buildLandingPageLink(
                          record.getMetadataIdentifier()))
                  .title(record.getResourceTitle().get(defaultText))
                  .build()).build()))
          .provenance(
              record.getResourceLineage().stream().map(l ->
                  Provenance.builder().provenanceStatement(
                      ProvenanceStatement.builder().label(l.get(defaultText)).build()
                  ).build()
              ).collect(Collectors.toList())
          )
          .type(record.getResourceType().stream().map(t ->
              new RdfResource(null, "dcat:" + RESSOURCE_TYPE_MAPPING.get(t), null))
              .collect(Collectors.toList()))
          // INSPIRE <dct:type rdf:resource="{$ResourceTypeCodelistUri}/{$ResourceType}"/>
          .modified(toDate(record.getChangeDate()))
          .theme(record.getCodelists().get(topic).stream().map(t -> Subject.builder()
              .skosConcept(SkosConcept.builder()
                  // TODO: rdf:resource="{$TopicCategoryCodelistUri}/{$TopicCategory}"
                  .prefLabel(t.getProperties().get(defaultText))
                  .build()).build()).collect(Collectors.toList()))
          .theme(record.getTag().stream().map(t -> Subject.builder()
              // TODO: <skos:ConceptScheme rdf:about="{$OriginatingControlledVocabularyURI}">
              // TODO: skos:inScheme
              // See https://github.com/SEMICeu/iso-19139-to-dcat-ap/blob/master/iso-19139-to-dcat-ap.xsl#L2803-L2864
              .skosConcept(SkosConcept.builder()
                  .prefLabel(t.get(defaultText))
                  .build()).build()).collect(Collectors.toList()));

      record.getResourceDate().stream()
          .filter(d -> "creation".equals(d.getType()))
          .forEach(d -> datasetBuilder.created(toDate(d.getDate())));

      record.getResourceDate().stream()
          .filter(d -> "publication".equals(d.getType()))
          .forEach(d -> datasetBuilder.issued(toDate(d.getDate())));

      record.getResourceDate().stream()
          .filter(d -> "revision".equals(d.getType()))
          .forEach(d -> datasetBuilder.modified(toDate(d.getDate())));

      // TODO: Convert to meter ?
      datasetBuilder.spatialResolutionInMeters(
          record.getResolutionScaleDenominator().stream()
              .map(BigDecimal::new).collect(Collectors.toList()));

      // INSPIRE
      if (record.getSpecificationConformance().size() > 0) {
        datasetBuilder.wasUsedBy(
            record.getSpecificationConformance().stream().map(c ->
                DcatActivity.builder().activity(
                    // TODO: Check RDF encoding
                    // https://github.com/SEMICeu/iso-19139-to-dcat-ap/blob/master/iso-19139-to-dcat-ap.xsl#L837-L840
                    ProvActivity.builder()
                        .used(
                            new RdfResource(null, resourceIdentifierUri, null)
                        )
                        .qualifiedAssociation(
                            ProvQualifiedAssociation.builder()
                                .hadPlan(ProvHadPlan.builder()
                                    .wasDerivedFrom(
                                        new RdfResource("Resource", null, null, c.getTitle(), null))
                                    .build())
                                .build()
                        )
                        .generated(
                            ProvGenerated.builder()
                                .type(new RdfResource(
                                    "http://inspire.ec.europa.eu/metadata-codelist/DegreeOfConformity/"
                                        + INSPIRE_DEGREE_OF_CONFORMITY.get(c.getPass()),
                                    null))
                            //RDFParseException: unexpected literal
                            //.description(c.getExplanation())
                            .build()
                        )
                        .build()).build()
            ).collect(Collectors.toList())
        );
      }

      //datasetBuilder.conformsTo(new RdfResource(null, "http://data.europa.eu/r5r/", null));
      // http://data.europa.eu/930/
      // dct:source used to link to metadata standard ?
      // (https://github.com/SEMICeu/iso-19139-to-dcat-ap/blob/master/iso-19139-to-dcat-ap.xsl#L955-L991)
      // datasetBuilder.source()

      if (record.getResourceLanguage() != null) {
        // TODO: Where to put resource language ?
        datasetBuilder.language(record.getResourceLanguage().stream().map(l ->
            new RdfResource(null,
                "http://publications.europa.eu/resource/authority/language/"
                    + l.toUpperCase(), null)).collect(Collectors.toList()));
      }

      ArrayList<Codelist> updateFrequencyList = record.getCodelists()
          .get(Codelists.maintenanceAndUpdateFrequency);
      if (updateFrequencyList != null && updateFrequencyList.size() > 0) {
        datasetBuilder.accrualPeriodicity(
            new RdfResource(
                null,
                ACCRUAL_PERIODICITY_URI_PREFIX
                    + ACCRUAL_PERIODICITY_TO_ISO
                    .get(updateFrequencyList.get(0)
                        .getProperties().get(CommonField.key)), null));
      }

      // <dct:spatial rdf:parseType="Resource">
      datasetBuilder.spatial(record.getGeometries().stream().map(g -> DctSpatial.builder()
          .location(DctLocation.builder().geometry(g).build()).build()).collect(
          Collectors.toList()));

      datasetBuilder.temporal(
          record.getResourceTemporalExtentDateRange().stream().map(range -> {
            DctPeriodOfTimeBuilder periodOfTime = DctPeriodOfTime.builder();
            if (StringUtils.isNotEmpty(range.getGte())) {
              periodOfTime.startDate(toDate(range.getGte()));
            }
            if (StringUtils.isNotEmpty(range.getLte())) {
              periodOfTime.endDate(toDate(range.getLte()));
            }
            return DctTemporal.builder()
                .periodOfTime(periodOfTime.build())
                .build();
          }).collect(Collectors.toList()));

      record.getLinks().stream().forEach(link -> {
        DcatDistributionBuilder dcatDistributionBuilder = DcatDistribution.builder()
            .title(listOfNullable(link.getName()))
            .description(listOfNullable(link.getDescription()))
            // TODO <dcat:accessService rdf:parseType="Resource">...
            // TODO: representation technique = gmd:MD_SpatialRepresentationTypeCode?
            .representationTechnique(Subject.builder()
                .skosConcept(SkosConcept.builder()
                    .prefLabel(link.getProtocol()).build()).build());

        // TODO: depending on function/protocol build page/accessUrl/downloadUrl
        dcatDistributionBuilder.accessUrl(link.getUrl());

        datasetBuilder.distribution(listOfNullable(DcatDistributionContainer.builder()
            .distribution(dcatDistributionBuilder.build()).build()));
      });

      datasetBuilder.contactPoint(
          record.getContactForResource().stream().map(contact ->
              DcatContactPoint.builder()
                  .contact(VcardContact.builder()
                      .title(contact.getOrganisation())
                      .role(contact.getRole())
                      .hasEmail(contact.getEmail()).build()).build()
          ).collect(Collectors.toList()));


      dcatDataset = datasetBuilder.build();


      catalogRecord = CatalogRecord.builder()
          .identifier(listOfNullable(record.getMetadataIdentifier()))
          .created(toDate(record.getCreateDate()))
          .modified(toDate(record.getChangeDate()))
          .language(listOfNullable(new RdfResource(null,
              "http://publications.europa.eu/resource/authority/language/"
                  + record.getMainLanguage().toUpperCase())))
          .primaryTopic(listOfNullable(new ResourceContainer(dcatDataset, null))).build();

    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }


    return catalogRecord;
  }

  private static Date toDate(String date) {
    return Date.from(
        Instant.from(
            DateTimeFormatter.ISO_DATE_TIME.parse(date)));
  }

  private static <E> List<E> listOfNullable(E e) {
    if (e == null) {
      return Collections.emptyList();
    }
    return Collections.singletonList(e);
  }

}
