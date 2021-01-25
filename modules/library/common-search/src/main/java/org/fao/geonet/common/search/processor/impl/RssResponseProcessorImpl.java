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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.commons.lang.StringUtils;
import org.fao.geonet.common.search.domain.UserInfo;
import org.fao.geonet.common.search.processor.SearchResponseProcessor;
import org.fao.geonet.index.model.IndexRecord;
import org.fao.geonet.index.model.ResourceDate;
import org.fao.geonet.index.model.rss.Guid;
import org.fao.geonet.index.model.rss.Item;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RssResponseProcessorImpl implements SearchResponseProcessor {

  @Value("${gn.baseurl}")
  String baseUrl;

  public DateTimeFormatter rssDateFormat = DateTimeFormatter.RFC_1123_DATE_TIME;

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
    // TODO: Get Collection info
    // And build it from metadata record if set
    String title = "GeoNetwork opensource";
    String link = "http://localhost:8080/geonetwork";
    String description = "Search for datasets, services and maps...";

    // The name of the channel.
    // It's how people refer to your service.
    // If you have an HTML website that contains the same information
    // as your RSS file, the title of your channel should be
    // the same as the title of your website.
    generator.writeStartElement("title");
    generator.writeCharacters(title);
    generator.writeEndElement();

    // The URL to the HTML website corresponding to the channel.
    generator.writeStartElement("link");
    generator.writeCharacters(link);
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


  /**
   * GeoNetwork 3 implementation:
   * See https://github.com/geonetwork/core-geonetwork/blob/master/web/src/main/webapp/xslt/services/rss/rss-utils.xsl
   *
   * <p>Differences:
   * * No GeoRSS support
   * * Link only target the landing page of the record
   */
  private Item toRssItem(ObjectNode doc) {
    try {
      IndexRecord record = new ObjectMapper()
          .readValue(doc.get("_source").toString(), IndexRecord.class);

      // https://www.rssboard.org/rss-specification#hrelementsOfLtitemgt
      Item item = new Item();
      Guid guid = new Guid();
      guid.setValue(record.getMetadataIdentifier());
      item.setGuid(guid);
      item.setTitle(record.getResourceTitle().get("default"));
      item.setDescription(record.getResourceAbstract().get("default"));
      item.setLink(buildLandingPageLink(record));

      // Email address of the author of the item.
      record.getContact().forEach(c ->
          item.setAuthor(c.getEmail())
      );

      // Includes the item in one or more categories.
      // Category could be tag ? Was hardcoded in GN3

      // Indicates when the item was published.
      // Publication date first, any resource date and fallback to record date
      List<ResourceDate> resourceDates = record.getResourceDate();
      Optional<ResourceDate> publicationDate = resourceDates.stream()
          .filter(d -> "publication".equals(d.getType())).findFirst();
      Optional<ResourceDate> firstDate = resourceDates.stream().findFirst();
      String pubDate;
      if (publicationDate.isPresent()) {
        pubDate = publicationDate.get().getDate();
      } else if (firstDate.isPresent()) {
        pubDate = firstDate.get().getDate();
      } else {
        pubDate = record.getDateStamp();
      }
      if (StringUtils.isNotEmpty(pubDate)) {
        item.setPubDate(OffsetDateTime.parse(pubDate).format(rssDateFormat));
      }

      return item;
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  private String buildLandingPageLink(IndexRecord record) {
    return String.format("%s/collections/%s/items/%s",
        baseUrl,
        "main",
        record.getMetadataIdentifier());
  }
}
