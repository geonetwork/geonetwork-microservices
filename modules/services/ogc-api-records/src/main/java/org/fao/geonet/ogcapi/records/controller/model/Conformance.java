package org.fao.geonet.ogcapi.records.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;

/**
 * Conformance entity.
 */
@JacksonXmlRootElement(localName = "Conformance")
@XmlRootElement(name = "Conformance")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Conformance {

  @JsonProperty("conformsTo")
  @JacksonXmlProperty(localName = "conformsTo")
  private List<String> conformsTo = new ArrayList<>();
}

