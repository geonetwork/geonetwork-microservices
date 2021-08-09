package org.fao.geonet.common.search.processor.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.google.common.base.Throwables;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.xml.stream.XMLStreamWriter;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.Serializer.Property;
import org.fao.geonet.common.search.domain.UserInfo;
import org.fao.geonet.common.xml.XsltUtil;
import org.fao.geonet.domain.Metadata;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames;
import org.fao.geonet.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;


@Component("XmlResponseProcessorImpl")
public class XmlResponseProcessorImpl extends AbstractResponseProcessor {

  @Autowired
  MetadataRepository metadataRepository;

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

    String transformation = "copy";

    generator.writeStartDocument("UTF-8", "1.0");
    {
      JsonParser parser = parserForStream(streamFromServer);

      List<Integer> ids = new ArrayList<>();
      ResponseParser responseParser = new ResponseParser();
      responseParser.matchHits(parser, generator, doc -> {
        ids.add(doc
            .get(IndexRecordFieldNames.source)
            .get(IndexRecordFieldNames.id).asInt());
      }, false);

      List<Metadata> records = metadataRepository.findAllById(ids);

      generator.writeStartElement("items");
      generator.writeAttribute("total", responseParser.total + "");
      generator.writeAttribute("relation", responseParser.totalRelation + "");
      generator.writeAttribute("took", responseParser.took + "");
      generator.writeAttribute("returned", records.size() + "");
      {
        records.forEach(r -> {
          String xsltFileName = String.format(
              "xslt/ogcapir/formats/%s/%s-%s.xsl",
              transformation, transformation, r.getDataInfo().getSchemaId(), transformation);
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
}
