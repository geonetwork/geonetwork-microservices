package org.fao.geonet.index.model.dcat2;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class RdfLiteral {
  @XmlAttribute
  String lang;

  @XmlValue
  String value;
}
