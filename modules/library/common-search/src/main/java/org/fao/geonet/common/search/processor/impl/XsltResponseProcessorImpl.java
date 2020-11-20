/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.common.search.processor.impl;

import com.fasterxml.jackson.core.JsonParser;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLStreamWriter;
import lombok.Getter;
import lombok.Setter;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.Serializer.Property;
import org.fao.geonet.common.search.Constants.IndexFieldNames;
import org.fao.geonet.common.search.domain.UserInfo;
import org.fao.geonet.common.search.processor.SearchResponseProcessor;
import org.fao.geonet.common.xml.XsltUtil;
import org.fao.geonet.domain.Metadata;
import org.fao.geonet.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class XsltResponseProcessorImpl implements SearchResponseProcessor {
  @Autowired
  MetadataRepository metadataRepository;

  @Getter
  @Setter
  private String transformation = "copy";

  /**
   * Process the search response and return RSS feed.
   *
   */
  public void processResponse(HttpSession httpSession,
      InputStream streamFromServer, OutputStream streamToClient,
      UserInfo userInfo, String bucket, boolean addPermissions) throws Exception {

    Processor p = new Processor(false);
    Serializer s = p.newSerializer();
    s.setOutputProperty(Property.INDENT, "no");
    s.setOutputStream(streamToClient);
    XMLStreamWriter generator = s.getXMLStreamWriter();

    String xsltFileName = String.format(
        "xslt/collections/items/formats/%s.xsl", transformation);
    File xsltFile = new ClassPathResource(xsltFileName).getFile();

    if (!xsltFile.exists()) {
      throw new IllegalArgumentException(String.format(
          "Transformation '%s' does not exist.", transformation
      ));
    }

    generator.writeStartDocument("UTF-8", "1.0");
    {
      JsonParser parser = ResponseParser.jsonFactory.createParser(streamFromServer);
      parser.nextToken();

      List<Integer> ids = new ArrayList<>();
      new ResponseParser().matchHits(parser, generator, doc -> {
        ids.add(doc.get(IndexFieldNames.SOURCE).get(IndexFieldNames.ID).asInt());
      });

      List<Metadata> records = metadataRepository.findAllById(ids);

      generator.writeStartElement("results");
      generator.writeAttribute("total", records.size() + "");
      {
        records.forEach(r -> {
          XsltUtil.transformAndStreamInDocument(
              r.getData(),
              xsltFile,
              generator
          );
        });
      }
      generator.writeEndElement();
    }
    generator.writeEndDocument();
    generator.flush();
    generator.close();
  }
}
