package org.fao.geonet.index.model.dcat2;

import static org.fao.geonet.index.model.dcat2.Namespaces.FOAF_URI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * A textual resource intended for human consumption that contains information, e.g., a Web page about a Dataset.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Data
@Builder
public class DcatDocument {

  @XmlElement(name = "Document", namespace = FOAF_URI)
  FoafDocument foafDocument;
}
