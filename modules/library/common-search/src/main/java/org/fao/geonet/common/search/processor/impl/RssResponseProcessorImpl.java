/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.common.search.processor.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.fao.geonet.common.search.domain.UserInfo;
import org.fao.geonet.common.search.processor.SearchResponseProcessor;
import org.fao.geonet.index.model.IndexRecord;
import org.fao.geonet.index.model.rss.Guid;
import org.fao.geonet.index.model.rss.Item;
import org.springframework.stereotype.Component;

@Component
public class RssResponseProcessorImpl implements SearchResponseProcessor {

  /**
   * Process the search response and return RSS feed.
   *
   */
  public void processResponse(HttpSession httpSession,
      InputStream streamFromServer, OutputStream streamToClient,
      UserInfo userInfo, String bucket, boolean addPermissions) throws Exception {
    XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
    XMLStreamWriter generator = xmlOutputFactory.createXMLStreamWriter(streamToClient);
    generator.writeStartDocument("UTF-8", "1.0");
    generator.writeStartElement("rss");
    generator.writeAttribute("version", "2.0");

    generator.writeStartElement("channel");
    writeChannelProperties(generator);

    JsonParser parser = ResponseParser.jsonFactory.createParser(streamFromServer);
    parser.nextToken();

    new ResponseParser().matchHits(parser, generator, doc -> {
      writeItem(generator, streamToClient, doc);
    });

    generator.writeEndElement();
    generator.writeEndElement();
    generator.writeEndDocument();
    generator.flush();
    generator.close();
  }

  private void writeChannelProperties(XMLStreamWriter generator) throws XMLStreamException {
    String title = "GeoNetwork opensource";
    String link = "http://localhost:8080/geonetwork";
    String description = "Search for datasets, services and maps...";

    generator.writeStartElement("title");
    generator.writeCharacters(title);
    generator.writeEndElement();
    generator.writeStartElement("link");
    generator.writeCharacters(link);
    generator.writeEndElement();
    generator.writeStartElement("description");
    generator.writeCharacters(description);
    generator.writeEndElement();
    generator.flush();
  }

  private void writeItem(XMLStreamWriter generator,
      OutputStream stream, ObjectNode doc) throws XMLStreamException {

    Item item = toRssItem(doc);
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(Item.class);
      OutputStreamWriter osw = new OutputStreamWriter(stream);
      Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
      marshaller.marshal(item, osw);
      osw.flush();
    } catch (JAXBException | IOException e) {
      e.printStackTrace();
    }
  }


  private Item toRssItem(ObjectNode doc) {
    try {
      IndexRecord record = new ObjectMapper()
          .readValue(doc.get("_source").toString(), IndexRecord.class);
      Item item = new Item();
      Guid guid = new Guid();
      guid.setValue(record.getId());
      item.setGuid(guid);
      item.setTitle(record.getResourceTitle().get("default"));
      item.setDescription(record.getResourceAbstract().get("default"));
      return item;
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }
}
