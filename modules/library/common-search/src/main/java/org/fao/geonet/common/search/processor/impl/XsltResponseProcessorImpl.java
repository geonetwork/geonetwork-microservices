/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.common.search.processor.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.Serializer.Property;
import org.fao.geonet.common.XsltUtil;
import org.fao.geonet.common.search.Constants.IndexFieldNames;
import org.fao.geonet.common.search.domain.UserInfo;
import org.fao.geonet.common.search.processor.SearchResponseProcessor;
import org.fao.geonet.domain.Metadata;
import org.fao.geonet.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class XsltResponseProcessorImpl implements SearchResponseProcessor {
  @Autowired
  MetadataRepository metadataRepository;

  /**
   * Process the search response and return RSS feed.
   *
   */
  public void processResponse(HttpSession httpSession,
      InputStream streamFromServer, OutputStream streamToClient,
      UserInfo userInfo, String bucket, boolean addPermissions) throws Exception {

    //    Processor p = new Processor(false);
    //    Serializer s = p.newSerializer();
    //    s.setOutputProperty(Property.INDENT, "no");
    //    serializer.setOutputStream(streamToClient);
    //    XMLStreamWriter generator = s.getXMLStreamWriter();

    XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
    xmlOutputFactory.setProperty("javax.xml.stream.isRepairingNamespaces", true);
    XMLStreamWriter generator = xmlOutputFactory.createXMLStreamWriter(streamToClient);

    generator.writeStartDocument("UTF-8", "1.0");

    JsonParser parser = ResponseParser.jsonFactory.createParser(streamFromServer);
    parser.nextToken();

    List<Integer> ids = new ArrayList<>();
    new ResponseParser().matchHits(parser, generator, doc -> {
      ids.add(doc.get(IndexFieldNames.SOURCE).get(IndexFieldNames.ID).asInt());
    });

    List<Metadata> records = metadataRepository.findAllById(ids);

    // TODO: Use a parameter for xslt
    String xsltFileName = String.format(
        "xslt/%s.xsl", "copy");
    File xsltFile = new ClassPathResource(xsltFileName).getFile();

    generator.writeStartElement("results");
    generator.writeAttribute("total", records.size() + "");

    records.forEach(r -> {
      XsltUtil.transform(
          r.getData(),
          xsltFile,
          generator
      );

      //      try {
      //        generator.writeCharacters(XsltUtil.transformXmlToString(r.getData(), xsltFile));
      //      } catch (XMLStreamException ioException) {
      //        ioException.printStackTrace();
      //      }
    });
    //    generator.writeEndElement();
    //    generator.writeEndDocument();
    generator.flush();
    generator.close();
  }
}
