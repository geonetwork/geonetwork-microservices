/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.common;


import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBResult;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;
import net.sf.saxon.s9api.Xslt30Transformer;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;
import net.sf.saxon.stax.XMLStreamWriterDestination;
import org.springframework.stereotype.Component;

@Component
public class XsltUtil {

  /**
   * Transform XML string to Object.
   */
  public static <T> T transformXmlToObject(
      String inputXmlString,
      File xsltFile,
      Class<T> objectClass
  ) {
    TransformerFactory factory = new net.sf.saxon.TransformerFactoryImpl();
    StreamSource xslt = new StreamSource(xsltFile);
    StreamSource text = new StreamSource(new StringReader(inputXmlString));
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(objectClass);
      JAXBResult result = new JAXBResult(jaxbContext);

      Transformer transformer = factory.newTransformer(xslt);
      transformer.transform(text, result);
      Object o = result.getResult();
      if (objectClass.isInstance(o)) {
        return (T) o;
      } else {
        return null;
      }
    } catch (TransformerConfigurationException e) {
      e.printStackTrace();
    } catch (TransformerException e2) {
      e2.printStackTrace();
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Transform XML string and write result as String.
   */
  public static String transformXmlToString(
      String inputXmlString,
      File xsltFile) {
    //    try {
    //      Processor proc = new Processor(false);
    //      XsltCompiler compiler = proc.newXsltCompiler();
    //      XsltExecutable xsl = compiler.compile(new StreamSource(xsltFile));
    //      Xslt30Transformer transformer = xsl.load30();
    //
    //      StringWriter stringWriter = new StringWriter();
    //      Serializer out = proc.newSerializer();
    //      out.setOutputProperty(Serializer.Property.METHOD, "xml");
    //      out.setOutputProperty(Serializer.Property.INDENT, "no");
    //
    //      transformer.transform(
    //          new StreamSource(new StringReader(inputXmlString)),
    //          out);
    //
    //      return stringWriter.toString();
    //    } catch (SaxonApiException e) {
    //      e.printStackTrace();
    //    }
    //    return inputXmlString;
    return null;
  }

  /**
   * Transform XML string and write result to XMLStreamWriter.
   */
  public static void transform(
      String inputXmlString,
      File xsltFile,
      XMLStreamWriter streamWriter) {
    try {
      Processor proc = new Processor(false);
      XsltCompiler compiler = proc.newXsltCompiler();

      XsltExecutable xsl = compiler.compile(new StreamSource(xsltFile));
      XsltTransformer transformer = xsl.load();

      XdmNode source = proc.newDocumentBuilder().build(
          new StreamSource(new StringReader(inputXmlString)));
      transformer.setInitialContextNode(source);
      transformer.setDestination(new XMLStreamWriterDestination(streamWriter));
      transformer.transform();

      //      Processor proc = new Processor(false);
      //      XsltCompiler compiler = proc.newXsltCompiler();
      //      XsltExecutable xsl = compiler.compile(new StreamSource(xsltFile));
      //      Xslt30Transformer transformer = xsl.load30();
      //      XMLStreamWriterDestination destination =
      //          new XMLStreamWriterDestination(streamWriter);
      //      transformer.transform(
      //          new StreamSource(new StringReader(inputXmlString)), destination
      //          );

    } catch (SaxonApiException e) {
      e.printStackTrace();
    }
  }
}
