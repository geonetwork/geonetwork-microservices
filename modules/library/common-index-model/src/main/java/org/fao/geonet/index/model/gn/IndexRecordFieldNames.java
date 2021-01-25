/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.index.model.gn;

public class IndexRecordFieldNames {
  public static final String isHarvested = "isHarvested";
  public static final String isPublishedToAll = "isPublishedToAll";
  public static final String resourceTitle = "resourceTitleObject";
  public static final String resourceAbstract = "resourceAbstractObject";
  public static final String org = "Org";
  public static final String orgForResource = "OrgForResource";
  public static final String recordLink = "recordLink";
  public static final String link = "link";
  public static final String coordinateSystem = "coordinateSystem";
  public static final String geom = "geom";
  public static final String location = "location";
  public static final String allKeywords = "allKeywords";
  public static final String tag = "tag";
  public static final String serviceType = "serviceType";
  public static final String format = "format";
  public static final String resourceDate = "resourceDate";
  public static final String resourceTemporalDateRange = "resourceTemporalDateRange";
  public static final String revisionYearForResource = "revisionYearForResource";
  public static final String revisionMonthForResource = "revisionMonthForResource";

  public class Codelists {
    public static final String prefix = "cl_";
    public static final String characterSet = prefix + "characterSet";
    public static final String hierarchyLevel = prefix + "hierarchyLevel";
    public static final String status = prefix + "status";
  }

  public class CommonField {
    public static final String defaultText = "default";
    public static final String key = "key";
    public static final String link = "link";
  }
}
