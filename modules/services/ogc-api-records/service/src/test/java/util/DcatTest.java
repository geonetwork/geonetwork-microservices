package util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.fao.geonet.index.model.dcat2.Concept;
import org.fao.geonet.index.model.dcat2.Dataset;
import org.fao.geonet.index.model.dcat2.Subject;
import org.junit.Test;

public class DcatTest {

  @Test
  public void testJsonToDcat() throws IOException {
    Dataset dcatDataset = new Dataset();

    ArrayList<String> titles = new ArrayList<>();
    titles.add("Title");
    dcatDataset.setTitle(titles);

    ArrayList<Subject> subjects = new ArrayList<>();
    subjects.add(new Subject(new Concept("http://about/1", "Label")));
    dcatDataset.setSubject(subjects);

    // POJO to XML
    JAXBContext context = null;
    String dcatXml = null;
    try {
      StringWriter sw = new StringWriter();
      context = JAXBContext.newInstance(Concept.class, Dataset.class);

      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
      marshaller.marshal(dcatDataset, sw);
      dcatXml = sw.toString();
      System.out.println(dcatXml);
    } catch (JAXBException e) {
      e.printStackTrace();
    }


    org.eclipse.rdf4j.model.Model model = Rio.parse(
        new ByteArrayInputStream(dcatXml.getBytes()),
        "", RDFFormat.RDFXML);
    // TODO name, abstract properties are missing in the model. Why?
    Rio.write(model, System.out, RDFFormat.RDFXML);
    Rio.write(model, System.out, RDFFormat.TURTLE);
    Rio.write(model, System.out, RDFFormat.JSONLD);
  }
}
