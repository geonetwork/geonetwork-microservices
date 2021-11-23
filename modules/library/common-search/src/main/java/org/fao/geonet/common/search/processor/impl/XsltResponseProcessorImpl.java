/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.common.search.processor.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.google.common.base.Throwables;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLStreamWriter;
import lombok.Getter;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.Serializer.Property;
import org.fao.geonet.common.search.GnMediaType;
import org.fao.geonet.common.search.domain.UserInfo;
import org.fao.geonet.common.xml.XsltUtil;
import org.fao.geonet.domain.Metadata;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames;
import org.fao.geonet.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component("XsltResponseProcessorImpl")
public class XsltResponseProcessorImpl extends AbstractResponseProcessor {

  @Autowired
  MetadataRepository metadataRepository;

  @Getter
  private String transformation = "copy";

  static final Map<String, String>
          ACCEPT_FORMATTERS =
          Map.of(
                  GnMediaType.APPLICATION_GN_XML_VALUE, "copy",
                  "gn", "copy",
                  GnMediaType.APPLICATION_DCAT2_XML_VALUE, "dcat",
                  "dcat", "dcat"
          );


  /**
   * Process the search response and return RSS feed.
   */
  public void processResponse(HttpSession httpSession,
      InputStream streamFromServer, OutputStream streamToClient,
      UserInfo userInfo, String bucket, Boolean addPermissions) throws Exception {

    Processor p = new Processor(false);
    Serializer s = p.newSerializer();
    s.setOutputProperty(Property.INDENT, "no");
    s.setOutputStream(streamToClient);
    XMLStreamWriter generator = s.getXMLStreamWriter();

    generator.writeStartDocument("UTF-8", "1.0");
    {
      JsonParser parser = parserForStream(streamFromServer);

      List<Integer> ids = new ArrayList<>();
      new ResponseParser().matchHits(parser, generator, doc -> {
        ids.add(doc
            .get(IndexRecordFieldNames.source)
            .get(IndexRecordFieldNames.id).asInt());
      }, false);

      List<Metadata> records = metadataRepository.findAllById(ids);

      generator.writeStartElement("results");
      generator.writeAttribute("total", records.size() + "");
      {
        records.forEach(r -> {
          String xsltFileName = String.format(
              "xslt/ogcapir/formats/%s/%s-%s.xsl",
              transformation, transformation, r.getDataInfo().getSchemaId());
          try (InputStream xsltFile =
              new ClassPathResource(xsltFileName).getInputStream()) {
            //  if (!xsltFile.exists()) {
            //    throw new IllegalArgumentException(String.format(
            //        "Transformation '%s' does not exist for schema %s.", transformation
            //    ));
            //  }

            XsltUtil.transformAndStreamInDocument(
                r.getData(),
                xsltFile,
                generator
            );
          } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
            throw new RuntimeException(e);
          }
        });
      }
      generator.writeEndElement();
    }
    generator.writeEndDocument();
    generator.flush();
    generator.close();
  }

  /**  affraid XsltResponseProcessorImpl is NOT reentrant.*/
  @Override
  public void setTransformation(String acceptHeader) {
    transformation = ACCEPT_FORMATTERS.get(acceptHeader);
  }
}
