/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.index.model;

public class IndexRecordFieldNames {

  public class Codelists {
    public static final String prefix = "CL_";
    public static final String characterSet = prefix + "characterSet";
    public static final String hierarchyLevel = prefix + "hierarchyLevel";
    public static final String status = prefix + "status";
  }

  public static final String resourceDate = "resourceDate";
  public static final String resourceTemporalDateRange = "resourceTemporalDateRange";
  public static final String revisionYearForResource = "revisionYearForResource";
  public static final String revisionMonthForResource = "revisionMonthForResource";
}
