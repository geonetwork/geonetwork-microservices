package org.fao.geonet.ogcapi.records.util;


import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtil {

  private XmlUtil() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Retrieves the content of a org.w3c.dom.Node as a string.
   */
  public static String getNodeString(Node node) throws TransformerException {
    StringWriter writer = new StringWriter();
    Transformer transformer = TransformerFactory.newInstance().newTransformer();
    transformer.transform(new DOMSource(node), new StreamResult(writer));
    return writer.toString();
  }


  /**
   * Parses a xml string in a org.w3c.dom.Document.
   */
  public static Document parseXmlString(String xmlContent)
      throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilder db = DocumentBuilderFactory.newInstance()
        .newDocumentBuilder();
    InputSource is = new InputSource();
    is.setCharacterStream(new StringReader(xmlContent));

    return db.parse(is);
  }
}
