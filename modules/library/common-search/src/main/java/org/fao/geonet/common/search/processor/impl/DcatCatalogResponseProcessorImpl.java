/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.common.search.processor.impl;

import static org.fao.geonet.index.model.dcat2.Namespaces.DCAT_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.DCAT_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.RDF_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.RDF_URI;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.fao.geonet.common.search.domain.UserInfo;
import org.fao.geonet.index.converter.DcatConverter;
import org.fao.geonet.index.converter.FormatterConfiguration;
import org.fao.geonet.index.converter.RssConverter;
import org.fao.geonet.index.model.dcat2.CatalogRecord;
import org.fao.geonet.index.model.dcat2.DataService;
import org.fao.geonet.index.model.dcat2.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("DcatCatalogResponseProcessorImpl")
@Slf4j(topic = "org.fao.geonet.searching")
public class DcatCatalogResponseProcessorImpl extends AbstractResponseProcessor {

  @Autowired
  private RssConverter rssConverter;

  @Autowired
  private FormatterConfiguration formatterConfiguration;

  @Autowired
  DcatConverter dcatConverter;

  /**
   * Process the search response and return RSS feed.
   */
  public void processResponse(HttpSession httpSession,
      InputStream streamFromServer, OutputStream streamToClient,
      UserInfo userInfo, String bucket, Boolean addPermissions) throws Exception {
    XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
    XMLStreamWriter generator = xmlOutputFactory.createXMLStreamWriter(streamToClient);
    generator.writeStartDocument("UTF-8", "1.0");
    generator.writeStartElement(RDF_PREFIX, "RDF", RDF_URI);
    generator.writeNamespace(RDF_PREFIX, RDF_URI);
    generator.writeCharacters("");
    generator.flush();

    JsonParser parser = parserForStream(streamFromServer);
    new ResponseParser().matchHits(parser, generator, doc -> {
      writeItem(generator, streamToClient, doc);
    }, false);

    generator.writeEndElement();
    generator.writeEndDocument();
    generator.flush();
    generator.close();
  }


  private void writeItem(XMLStreamWriter generator,
      OutputStream stream, ObjectNode doc) throws XMLStreamException {
    try {
      JAXBContext context = null;
      context = JAXBContext.newInstance(
          CatalogRecord.class, Dataset.class, DataService.class);
      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

      CatalogRecord catalogRecord = dcatConverter.convert(doc);
      StringWriter sw = new StringWriter();
      marshaller.marshal(catalogRecord, sw);

      String dcatXml = sw.toString();
      OutputStreamWriter osw = new OutputStreamWriter(stream);
      osw.write(dcatXml);
      osw.flush();
    } catch (JAXBException | IOException e) {
      String msg = String.format("Unable to parse document \"%s\"...:",
          doc.toString().substring(0, 90));
      log.error(msg, e);
    }
  }
}
