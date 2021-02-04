/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.gn;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Values {

  @XmlElement
  private String key;
  @XmlElement
  private List<String> values = new ArrayList<>();

  public String getKey() {
    return key;
  }

  public void setKey(String value) {
    key = value;
  }

  public List<String> getValues() {
    return values;
  }

}
