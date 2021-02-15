

package org.fao.geonet.index.model.dcat2;

import static org.fao.geonet.index.model.dcat2.Namespaces.DCAT_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.DCT_URI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * GeoDCAT-AP 2 model.
 *
 * <p>https://www.w3.org/TR/vocab-dcat-2/
 * https://semiceu.github.io/GeoDCAT-AP/drafts/latest/geodcat-ap_v2.0.0.svg
 */
@XmlRootElement(namespace = DCAT_URI)
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dataset {
  @NonNull
  @XmlElement(namespace = DCT_URI)
  List<String> title = new ArrayList();

  @NonNull
  @XmlElement(namespace = DCT_URI)
  List<String> description = new ArrayList();

  @XmlElement(namespace = DCT_URI)
  List<String> identifier = new ArrayList();

  @XmlElement(namespace = DCT_URI)
  List<Subject> subject = new ArrayList();
}
