/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.fao.geonet.common;


import java.io.File;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBResult;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
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
}
