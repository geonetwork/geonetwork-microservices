/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.common.search.processor.impl;

import com.fasterxml.jackson.core.JsonParser;
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
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.common.search.domain.UserInfo;
import org.fao.geonet.index.converter.FormatterConfiguration;
import org.fao.geonet.index.converter.RssConverter;
import org.fao.geonet.index.model.rss.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("RssResponseProcessorImpl")
@Slf4j(topic = "org.fao.geonet.searching")
public class RssResponseProcessorImpl extends AbstractResponseProcessor {

  @Autowired
  private RssConverter rssConverter;

  @Autowired
  private FormatterConfiguration formatterConfiguration;

  /**
   * Process the search response and return RSS feed.
   */
  public void processResponse(HttpSession httpSession,
      InputStream streamFromServer, OutputStream streamToClient,
      UserInfo userInfo, String bucket, Boolean addPermissions) throws Exception {
    XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
    XMLStreamWriter generator = xmlOutputFactory.createXMLStreamWriter(streamToClient);
    generator.writeStartDocument("UTF-8", "1.0");
    generator.writeStartElement("rss");
    generator.writeAttribute("version", "2.0");

    generator.writeStartElement("channel");
    writeChannelProperties(generator);

    JsonParser parser = parserForStream(streamFromServer);

    new ResponseParser().matchHits(parser, generator, doc -> {
      writeItem(generator, streamToClient, doc);
    }, false);

    generator.writeEndElement();
    generator.writeEndElement();
    generator.writeEndDocument();
    generator.flush();
    generator.close();
  }

  private void writeChannelProperties(XMLStreamWriter generator) throws XMLStreamException {
    // TODO: Get Collection info
    // And build it from metadata record if set
    String description = "Search for datasets, services and maps...";

    // The name of the channel.
    // It's how people refer to your service.
    // If you have an HTML website that contains the same information
    // as your RSS file, the title of your channel should be
    // the same as the title of your website.
    generator.writeStartElement("title");
    generator.writeCharacters(formatterConfiguration.getSourceSiteTitle());
    generator.writeEndElement();

    // The URL to the HTML website corresponding to the channel.
    generator.writeStartElement("link");
    generator.writeCharacters(formatterConfiguration.getSourceHomePage());
    generator.writeEndElement();

    // Phrase or sentence describing the channel.
    generator.writeStartElement("description");
    generator.writeCharacters(description);
    generator.writeEndElement();

    // Optional elements
    // https://www.rssboard.org/rss-specification#optionalChannelElements

    generator.flush();
  }

  private void writeItem(XMLStreamWriter generator,
      OutputStream stream, ObjectNode doc) throws XMLStreamException {


    try {
      Item item = rssConverter.convert(doc);

      JAXBContext jaxbContext = JAXBContext.newInstance(Item.class);
      OutputStreamWriter osw = new OutputStreamWriter(stream);
      Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
      marshaller.marshal(item, osw);
      osw.flush();
    } catch (JAXBException | IOException e) {
      String msg = String.format("Unable to parse document \"%s\"...:",
          doc.toString().substring(0, 90));
      log.error(msg, e);
    }
  }
}
