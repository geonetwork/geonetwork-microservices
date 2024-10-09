/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * GN Elastic JSON index metadata. This describes some elastic index JSON paths and how to use them
 * for searching for a Queryable.
 *
 * <p>Consult your local elastic index for how its built - http://localhost:9200/gn-records
 *
 * <p>A Queryable can be referenced to multiple columns.
 *
 * <p>Examples:
 * 1. resourceTitleObject This has a bunch of sub-components.  For example, "default", "langeng",
 * "langdut". 2.
 *
 * <p>Also, note VERY BRIEFLY, elastic has 2 types of "columns" - keyword and text. text can
 * be searched generally (i.e. fuzzy full text search), while keywords must be EQUAL (including case
 * and must be a full, exact, match).
 *
 * <p>Elastic indexing is very rich - read the elastic documentation and consult to actual
 * gn-records elastic index.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class GnElasticInfo {

  /**
   * a JSON path that contains the queryable's info.
   *
   * <p>note - you can use ${lang3iso} in this path to define a language specific property.
   *
   * <p>i.e. "resourceTitleObject.lang${lang3iso}" would be converted to something like
   * "resourceTitleObject.langeng" or "resourceTitleObject.langdut" etc...
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "elasticPath")
  @XmlElement(name = "elasticPath")
  private String elasticPath;
  /**
   * how is that location indexed?  This is useful for hints to the UI.
   *
   * <p>TEXT - can do full text search.  KEYWORD - must be exact match (with some fuzzyness).
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "elasticColumnType")
  @XmlElement(name = "elasticColumnType")
  private ElasticColumnType elasticColumnType;
  /**
   * how is that location indexed?  This is useful for hints to the UI.
   *
   * <p>LOWERCASE = used the lowercase normalizer
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "elasticColumnFormat")
  @XmlElement(name = "elasticColumnFormat")
  private ElasticColumnFormat elasticColumnFormat;
  /**
   * What type of query to use?
   *
   * <p>MULTIMATCH - use an elastic `multi_match` (recommended for everything)
   *
   * <p>NESTED - some object trees prefer elastic `nested` searchs.  "contacts" prefers this.
   */
  @JsonInclude(Include.NON_EMPTY)
  @XmlElementWrapper(name = "elasticColumnFormat")
  @XmlElement(name = "elasticQueryType")
  private ElasticQueryType elasticQueryType = ElasticQueryType.MULTIMATCH;

  public GnElasticInfo(String elasticPath, ElasticColumnType elasticColumnType) {
    this(elasticPath, elasticColumnType, ElasticColumnFormat.NORMAL);
  }

  public GnElasticInfo() {
  }


  /**
   * build a gnElasticInfo with the main fields set.
   * @param elasticPath  Elastic JSON path
   * @param elasticColumnType what data type is this?
   * @param format Metadata for GUIs.
   */
  public GnElasticInfo(String elasticPath,
      ElasticColumnType elasticColumnType,
      ElasticColumnFormat format) {
    this.elasticPath = elasticPath;
    this.elasticColumnType = elasticColumnType;
    this.elasticColumnFormat = format;
  }

  //-------------------------------------

  public ElasticQueryType getElasticQueryType() {
    return elasticQueryType;
  }

  public void setElasticQueryType(ElasticQueryType elasticQueryType) {
    this.elasticQueryType = elasticQueryType;
  }

  public String getElasticPath() {
    return elasticPath;
  }

  //-------------------------------------

  public void setElasticPath(String elasticPath) {
    this.elasticPath = elasticPath;
  }

  public ElasticColumnType getElasticColumnType() {
    return elasticColumnType;
  }

  public void setElasticColumnType(ElasticColumnType elasticColumnType) {
    this.elasticColumnType = elasticColumnType;
  }

  public ElasticColumnFormat getElasticColumnFormat() {
    return elasticColumnFormat;
  }

  public void setElasticColumnFormat(ElasticColumnFormat elasticColumnFormat) {
    this.elasticColumnFormat = elasticColumnFormat;
  }

  public enum ElasticColumnType {
    KEYWORD, TEXT, OTHER, DATE, GEO, DATERANGE
  }

  public enum ElasticColumnFormat {
    NORMAL, LOWERCASE
  }

  public enum ElasticQueryType {
    NESTED, MULTIMATCH
  }
}
