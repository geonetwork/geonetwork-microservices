package util;

import static org.fao.geonet.index.model.dcat2.Dataset.ACCRUAL_PERIODICITY_TO_ISO;
import static org.fao.geonet.index.model.dcat2.Dataset.ACCRUAL_PERIODICITY_URI_PREFIX;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.fao.geonet.index.model.dcat2.AccrualPeriodicity;
import org.fao.geonet.index.model.dcat2.Dataset;
import org.fao.geonet.index.model.dcat2.DcatAccessRights;
import org.fao.geonet.index.model.dcat2.DcatDistribution;
import org.fao.geonet.index.model.dcat2.DcatDistributionContainer;
import org.fao.geonet.index.model.dcat2.DcatDocument;
import org.fao.geonet.index.model.dcat2.DcatLicenseDocument;
import org.fao.geonet.index.model.dcat2.DcatLicenseDocumentContainer;
import org.fao.geonet.index.model.dcat2.DcatQualifiedAttribution;
import org.fao.geonet.index.model.dcat2.DcatRelation;
import org.fao.geonet.index.model.dcat2.DcatRelationship;
import org.fao.geonet.index.model.dcat2.FoafDocument;
import org.fao.geonet.index.model.dcat2.Language;
import org.fao.geonet.index.model.dcat2.ProvAttribution;
import org.fao.geonet.index.model.dcat2.ProvenanceStatement;
import org.fao.geonet.index.model.dcat2.RdfResource;
import org.fao.geonet.index.model.dcat2.RightsStatement;
import org.fao.geonet.index.model.dcat2.SkosConcept;
import org.fao.geonet.index.model.dcat2.Subject;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames.CommonField;
import org.junit.Test;

;

public class DcatTest {

  @Test
  public void testJsonToDcat() throws IOException {
    String identifier = "1567-765175-6561756";
    Dataset dcatDataset = Dataset.builder()
        .title(List.of("Rivers on earth"))
        .description(List.of("Water drop ..."))
        .identifier(List.of(identifier))
        .subject(List.of(Subject.builder()
                .skosConcept(
                    SkosConcept.builder()
                        .about("https://registry.org/hydrology")
                        .prefLabel("Hydrology")
                        .build()).build(),
            Subject.builder()
                .skosConcept(SkosConcept.builder()
                    .prefLabel("Earth observation").build())
                .build()))
        .landingPage(List.of(DcatDocument.builder()
            .foafDocument(FoafDocument.builder()
                .about("https://data/file.zip")
                .title("Download dataset")
                .description("CC0")
                .format("Shapefile")
                .build()).build()))
        .qualifiedRelation(List.of(DcatRelationship.builder()
            .relation(DcatRelation.builder()
                .relation("http://...TODO")
                .build()).build()))
        .spatialResolutionInMeters(List.of(new BigDecimal(25000)))
        .temporalResolution(List.of(Duration.ofDays(15)))
        .accessRights(List.of(DcatAccessRights.builder()
            .rightsStatement(RightsStatement.builder()
                .label(
                    "public access limited according to Article 13(1)(b) of the INSPIRE Directive")
                .about("https://registry.inspire/...")
                .build()).build()))
        .accrualPeriodicity(
            new AccrualPeriodicity(new SkosConcept(
                ACCRUAL_PERIODICITY_URI_PREFIX + ACCRUAL_PERIODICITY_TO_ISO.get("daily"),
                null,
                "http://purl.org/dc/terms/Frequency",
                "daily")))
        .conformsTo(
            new RdfResource(null, "http://iso19115-3.schema.org", null)
        )
        .created(new Date())
        .isReferencedBy(List.of(new RdfResource(null, "https://isReferencedBy", null)))
        .relation(List.of(new RdfResource(null, "https://relation", null)))
        .language(List.of(
            new Language(new SkosConcept("http://publications.europa.eu/resource/authority/language/FRE", null, null))))
        .type(List.of(Subject.builder().skosConcept(
            SkosConcept.builder().prefLabel("dataset").build()
        ).build()))
        .page(List.of(DcatDocument.builder()
            .foafDocument(FoafDocument.builder()
                .about("https://apps.titellus.net/ogcapi/collections/main/items/" + identifier)
                .build()).build()))
        .versionInfo("1.0")
        .qualifiedAttribution(List.of(DcatQualifiedAttribution.builder()
            .attribution(ProvAttribution.builder()
                .agent(new RdfResource(null, "http://agent", null))
                .hadRole(new RdfResource(null, "http://role/creator", null))
                .build()).build()))
        .comment(List.of("Comments ..."))
        .distribution(List.of(DcatDistributionContainer.builder()
            .distribution(DcatDistribution.builder()
                .accessUrl(new RdfResource(null, "https://sdi.eea.europa.eu/webdav/continental/europe/natural_areas/birds_directive/eea_v_3035_10_mio_art12-2008-2012_i_2008-2012_v01_r01/Art12-2008-2012_SHP"))
                .build()).build()))
        .license(DcatLicenseDocumentContainer.builder()
            // TODO .resource("https://creativecommons.org/publicdomain/zero/1.0/deed")
            .license(DcatLicenseDocument.builder()
                .type(Subject.builder()
                    .skosConcept(SkosConcept.builder().prefLabel("CC0").build())
                    .build()).build())
            .build())
        .build();

    // POJO to XML
    JAXBContext context = null;
    String dcatXml = null;
    try {
      StringWriter sw = new StringWriter();
      context = JAXBContext.newInstance(SkosConcept.class, Dataset.class);

      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      marshaller.marshal(dcatDataset, sw);
      dcatXml = sw.toString();
    } catch (JAXBException e) {
      e.printStackTrace();
    }

    org.eclipse.rdf4j.model.Model model = Rio.parse(
        new ByteArrayInputStream(dcatXml.getBytes()),
        "", RDFFormat.RDFXML);
    //    Rio.write(model, System.out, RDFFormat.RDFXML);
    Rio.write(model, System.out, RDFFormat.TURTLE);
    //    Rio.write(model, System.out, RDFFormat.JSONLD);
  }
}
