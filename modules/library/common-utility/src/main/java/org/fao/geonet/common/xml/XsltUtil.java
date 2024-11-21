/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.common.xml;


import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;
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
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Xslt30Transformer;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
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
    TransformerFactory factory = XsltTransformerFactory.get();
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
   * Transform XML string and write result to XMLStreamWriter.
   */
  public static void transformAndStreamInDocument(
      String inputXmlString,
      InputStream xsltFile,
      XMLStreamWriter streamWriter,
      Map<QName, net.sf.saxon.s9api.XdmValue> xslParameters) {
    try {
      Processor proc = XsltTransformerFactory.getProcessor();
      XsltCompiler compiler = proc.newXsltCompiler();

      XsltExecutable xsl = compiler.compile(new StreamSource(xsltFile));
      Xslt30Transformer transformer = xsl.load30();
      if (xslParameters != null) {
        transformer.setStylesheetParameters(xslParameters);
      }
      transformer.transform(
          new StreamSource(new StringReader(inputXmlString)),
          new XmlStreamWriterDestinationInDocument(streamWriter));
    } catch (SaxonApiException e) {
      e.printStackTrace();
    }
  }

  /**
   * Transform XML string in OutputStream.
   */
  public static void transformXmlAsOutputStream(
      String inputXmlString,
      InputStream xsltFile,
      Map<QName, net.sf.saxon.s9api.XdmValue> xslParameters,
      OutputStream outputStream) {
    try {
      Processor proc = XsltTransformerFactory.getProcessor();
      XsltCompiler compiler = proc.newXsltCompiler();

      XsltExecutable xsl = compiler.compile(new StreamSource(xsltFile));
      Xslt30Transformer transformer = xsl.load30();
      if (xslParameters != null) {
        transformer.setStylesheetParameters(xslParameters);
      }
      transformer.transform(
          new StreamSource(new StringReader(inputXmlString)),
          proc.newSerializer(outputStream));
    } catch (SaxonApiException e) {
      e.printStackTrace();
    }
  }

  /**
   * Transform XML string as String.
   */
  public static String transformXmlAsString(
      String inputXmlString,
      InputStream xsltFile,
      Map<QName, net.sf.saxon.s9api.XdmValue> xslParameters) {
    try {
      Processor proc = XsltTransformerFactory.getProcessor();
      XsltCompiler compiler = proc.newXsltCompiler();

      XsltExecutable xsl = compiler.compile(new StreamSource(xsltFile));
      Xslt30Transformer transformer = xsl.load30();
      if (xslParameters != null) {
        transformer.setStylesheetParameters(xslParameters);
      }
      StringWriter stringWriter = new StringWriter();
      transformer.transform(
          new StreamSource(new StringReader(inputXmlString)),
          proc.newSerializer(stringWriter));
      return stringWriter.toString();
    } catch (SaxonApiException e) {
      e.printStackTrace();
    }
    return "";
  }
}
