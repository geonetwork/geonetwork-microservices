/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.gn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * represents a theme (inside the elastic json allKeywords).
 *
 *<p>"allKeywords": {
 *         "th_inspire-service-taxonomy": {
 *         "id": "geonetwork.thesaurus.external.theme.inspire-service-taxonomy",
 *         "theme": "theme",
 *         "link": "http://localhost:8080/geonetwork/srv/api/registries/vocabularies/external.theme.inspire-service-taxonomy",
 *         "keywords": [
 *             {
 *                  "default": "GEONETWORK",
 *                  "langeng": "GEONETWORK"
 *             },
 *             {
 *                  "default": "OGCAPI",
 *                  "langeng": "OGCAPI"
 *             }
 *        ]
 *      }
 * }
 *
 */
@Data
@EqualsAndHashCode()
@XmlRootElement(name = "indexRecord")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Theme {
  public String id;
  public String title;
  public String theme;
  public String link;
  public List<HashMap<String, String>> keywords;
  public HashMap<String,String> multilingualTitle;

}
