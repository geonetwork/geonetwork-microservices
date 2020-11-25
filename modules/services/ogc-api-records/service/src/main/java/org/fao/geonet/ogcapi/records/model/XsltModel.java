package org.fao.geonet.ogcapi.records.model;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.fao.geonet.domain.Source;

@Data
@NoArgsConstructor
@XmlRootElement(name = "model")
@XmlAccessorType(XmlAccessType.FIELD)
public class XsltModel {
  Map<String, String> i18n;
  Map<String, String> settings;

  @XmlElementWrapper(name = "collections")
  @XmlElement(name = "collection")
  List<Source> collections;
  Source collection;
  List<Object> items;

  /**
   * Convert to InputStream.
   */
  public InputStream toSource() {
    try {
      StringWriter sw = new StringWriter();
      JAXBContext context = JAXBContext.newInstance(
          XsltModel.class, Source.class);
      Marshaller marshaller = context.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
      marshaller.marshal(this, sw);
      return IOUtils.toInputStream(sw.toString());
    } catch (JAXBException e) {
      e.printStackTrace();
    }
    return null;
  }
}
