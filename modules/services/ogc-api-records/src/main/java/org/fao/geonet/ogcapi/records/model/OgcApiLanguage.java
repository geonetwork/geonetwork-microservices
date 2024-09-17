/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * cf. https://github.com/opengeospatial/ogcapi-records/blob/master/core/openapi/schemas/language.yaml
 *
 * <p>The language used for textual values in this record.
 */
public class OgcApiLanguage {

  /**
   * The language tag as per RFC-5646. example: el
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "code")
  @XmlElement(name = "code")
  public String code;

  /**
   * The untranslated name of the language. example: Ελληνικά
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "name")
  @XmlElement(name = "name")
  public String name;

  /**
   * The name of the language in another well-understood language, usually English. example: Greek
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "alternate")
  @XmlElement(name = "alternate")
  public String alternate;


  /**
   * The direction for text in this language. The default, `ltr` (left-to-right), represents the
   * most common situation. However, care should be taken to set the value of `dir` appropriately if
   * the language direction is not `ltr`. Other values supported are `rtl` (right-to-left), `ttb`
   * (top-to-bottom), and `btt` (bottom-to-top). enum: - ltr - rtl - ttb - btt
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "dir")
  @XmlElement(name = "dir")
  public String dir;


  public OgcApiLanguage(String code) {
    this.code = code;
  }


  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAlternate() {
    return alternate;
  }

  public void setAlternate(String alternate) {
    this.alternate = alternate;
  }

  public String getDir() {
    return dir;
  }

  public void setDir(String dir) {
    this.dir = dir;
  }
}
