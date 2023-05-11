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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang.StringUtils;
import org.fao.geonet.index.model.dcat2.AccrualPeriodicity;
import org.fao.geonet.index.model.dcat2.CatalogRecord;
import org.fao.geonet.index.model.dcat2.DataService;
import org.fao.geonet.index.model.dcat2.DataService.DataServiceBuilder;
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
import org.fao.geonet.index.model.dcat2.Language;
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
  @Autowired
  FormatterConfiguration formatterConfiguration;
  @Value("${gn.language.default:en}")
  private String defaultLanguage;

  private final ObjectMapper mapper = new ObjectMapper();

  private static Date toDate(String date) {
    try {
      return Date.from(
          Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(
              date.length() == 10 ? date + "T00:00:00" : date)));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  private static <E> List<E> listOfNullable(E e) {
    if (e == null) {
      return Collections.emptyList();
    }
    return Collections.singletonList(e);
  }

  /**
   * Convert an index document into a DCAT object.
   */
  public CatalogRecord convert(JsonNode doc) {
    CatalogRecord catalogRecord = null;
    Dataset dcatDataset = null;
    DataService dcatService = null;
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

      List<String> resourceLanguage = record.getResourceLanguage();

      List<String> resourceType = record.getResourceType();
      boolean isInspireResource = resourceType.contains("dataset")
          || resourceType.contains("series")
          || resourceType.contains("service");
      boolean isService = resourceType.contains("service");

      // TODO: Add multilingual support
      // TODO .resource("https://creativecommons.org/publicdomain/zero/1.0/deed")

      if (isService) {
        dcatService = getDataService(record, recordUri + "#service", resourceIdentifierUri);
      } else {
        dcatDataset = getDataset(record, recordUri + "#resource", resourceIdentifierUri);
      }

      catalogRecord = CatalogRecord.builder()
          .about(recordUri)
          .identifier(listOfNullable(record.getMetadataIdentifier()))
          .created(toDate(record.getCreateDate()))
          .modified(toDate(record.getChangeDate()))
          .language(listOfNullable(
              new Language(
                  new SkosConcept(
                      "http://publications.europa.eu/resource/authority/language/"
                          + language.toUpperCase(),
                      null,
                      "http://purl.org/dc/terms/LinguisticSystem",
                      language))))
          .primaryTopic(listOfNullable(new ResourceContainer(
              dcatDataset,
              dcatService))).build();

    } catch (JsonMappingException e) {
      e.printStackTrace();
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return catalogRecord;
  }

  private Dataset getDataset(IndexRecord record, String uri, String resourceIdentifierUri) {
    DatasetBuilder datasetBuilder = Dataset.builder()
        .about(uri)
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
                new Subject(new SkosConcept(null, null, RESSOURCE_TYPE_MAPPING.get(t))))
            .collect(Collectors.toList()))
        // INSPIRE <dct:type rdf:resource="{$ResourceTypeCodelistUri}/{$ResourceType}"/>
        .modified(toDate(record.getChangeDate()))
        .theme(
            Optional.ofNullable(record.getCodelists().get(topic))
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(t -> Subject.builder()
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
                                      new RdfResource("Resource", null, null, c.getTitle(),
                                          null))
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
      datasetBuilder.language(record.getResourceLanguage().stream().map(l ->
          new Language(new SkosConcept(
              "http://publications.europa.eu/resource/authority/language/"
                  + l.toUpperCase(),
              null,
              "http://purl.org/dc/terms/LinguisticSystem",
              l))).collect(Collectors.toList()));
    }

    ArrayList<Codelist> updateFrequencyList = record.getCodelists()
        .get(Codelists.maintenanceAndUpdateFrequency);
    if (updateFrequencyList != null && updateFrequencyList.size() > 0) {

      String frequencyKey = ACCRUAL_PERIODICITY_TO_ISO
          .get(updateFrequencyList.get(0)
              .getProperties().get(CommonField.key));

      datasetBuilder.accrualPeriodicity(
          new AccrualPeriodicity(new SkosConcept(
              ACCRUAL_PERIODICITY_URI_PREFIX
                  + (frequencyKey == null
                  ? updateFrequencyList.get(0)
                  .getProperties().get(CommonField.key) : frequencyKey),
              null,
              "http://purl.org/dc/terms/Frequency",
              frequencyKey)));
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
          .title(listOfNullable(link.getName().get(CommonField.defaultText)))
          .description(listOfNullable(link.getDescription().get(CommonField.defaultText)))
          // TODO <dcat:accessService rdf:parseType="Resource">...
          // TODO: representation technique = gmd:MD_SpatialRepresentationTypeCode?
          .representationTechnique(Subject.builder()
              .skosConcept(SkosConcept.builder()
                  .prefLabel(link.getProtocol()).build()).build());

      // TODO: depending on function/protocol build page/accessUrl/downloadUrl
      dcatDistributionBuilder.accessUrl(new RdfResource(null,
          link.getUrl().get(CommonField.defaultText)));

      datasetBuilder.distribution(listOfNullable(DcatDistributionContainer.builder()
          .distribution(dcatDistributionBuilder.build()).build()));
    });

    datasetBuilder.contactPoint(
        record.getContactForResource().stream().map(contact ->
            DcatContactPoint.builder()
                .contact(VcardContact.builder()
                    .title(contact.getOrganisation().get(CommonField.defaultText))
                    .role(contact.getRole())
                    .hasEmail(contact.getEmail()).build()).build()
        ).collect(Collectors.toList()));

    return datasetBuilder.build();
  }

  private DataService getDataService(IndexRecord record, String uri, String resourceIdentifierUri) {
    DataServiceBuilder dataServiceBuilder = DataService.builder()
        .about(uri)
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
        .type(record.getResourceType().stream().map(t ->
                new Subject(new SkosConcept(null, null, RESSOURCE_TYPE_MAPPING.get(t))))
            .collect(Collectors.toList()))
        // INSPIRE <dct:type rdf:resource="{$ResourceTypeCodelistUri}/{$ResourceType}"/>
        .modified(toDate(record.getChangeDate()))
        .theme(
            Optional.ofNullable(record.getCodelists().get(topic))
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(t -> Subject.builder()
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
        .forEach(d -> dataServiceBuilder.created(toDate(d.getDate())));

    record.getResourceDate().stream()
        .filter(d -> "publication".equals(d.getType()))
        .forEach(d -> dataServiceBuilder.issued(toDate(d.getDate())));

    record.getResourceDate().stream()
        .filter(d -> "revision".equals(d.getType()))
        .forEach(d -> dataServiceBuilder.modified(toDate(d.getDate())));

    // TODO: Convert to meter ?
    dataServiceBuilder.spatialResolutionInMeters(
        record.getResolutionScaleDenominator().stream()
            .map(BigDecimal::new).collect(Collectors.toList()));

    // INSPIRE
    if (record.getSpecificationConformance().size() > 0) {
      dataServiceBuilder.wasUsedBy(
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
                                      new RdfResource("Resource", null, null, c.getTitle(),
                                          null))
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

    if (record.getResourceLanguage() != null) {
      dataServiceBuilder.language(record.getResourceLanguage().stream().map(l ->
          new Language(new SkosConcept(
              "http://publications.europa.eu/resource/authority/language/"
                  + l.toUpperCase(),
              null,
              "http://purl.org/dc/terms/LinguisticSystem",
              l))).collect(Collectors.toList()));
    }

    ArrayList<Codelist> updateFrequencyList = record.getCodelists()
        .get(Codelists.maintenanceAndUpdateFrequency);
    if (updateFrequencyList != null && updateFrequencyList.size() > 0) {
      String frequencyKey = ACCRUAL_PERIODICITY_TO_ISO
          .get(updateFrequencyList.get(0)
              .getProperties().get(CommonField.key));
    }

    dataServiceBuilder.spatial(record.getGeometries().stream().map(g -> DctSpatial.builder()
        .location(DctLocation.builder().geometry(g).build()).build()).collect(
        Collectors.toList()));

    dataServiceBuilder.temporal(
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

    dataServiceBuilder.endpointUrl(
        record.getLinks().stream().map(l ->
            new RdfResource(null, l.getUrl().get(defaultText)))
            .collect(Collectors.toList()));

    dataServiceBuilder.servesDataset(
        Optional.ofNullable(record.getAssociatedRecords())
            .map(Collection::stream)
            .orElseGet(Stream::empty)
            .filter(l -> "dataset".equals(l.getType()))
            .map(l -> new RdfResource(l.getUrl(), "dcat:Dataset"))
            .collect(Collectors.toList()));

    dataServiceBuilder.contactPoint(
        record.getContactForResource().stream().map(contact ->
            DcatContactPoint.builder()
                .contact(VcardContact.builder()
                    .title(contact.getOrganisation().get(CommonField.defaultText))
                    .role(contact.getRole())
                    .hasEmail(contact.getEmail()).build()).build()
        ).collect(Collectors.toList()));

    return dataServiceBuilder.build();
  }

}
