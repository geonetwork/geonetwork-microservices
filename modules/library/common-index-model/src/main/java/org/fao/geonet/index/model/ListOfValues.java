package org.fao.geonet.index.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ListOfValues {
  @XmlElement
  private List<Values> values = new ArrayList<>();

  public List<Values> getValues() {
    return values;
  }
}
