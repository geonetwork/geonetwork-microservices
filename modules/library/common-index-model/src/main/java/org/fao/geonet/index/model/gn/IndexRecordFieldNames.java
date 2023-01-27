/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.gn;

public class IndexRecordFieldNames {

  public static final String source = "_source";

  public static final String id = "id";
  public static final String uuid = "metadataIdentifier";
  public static final String owner = "owner";
  public static final String groupOwner = "groupOwner";
  public static final String opPrefix = "op";

  public static final String isHarvested = "isHarvested";
  public static final String isPublishedToAll = "isPublishedToAll";
  public static final String resourceTitle = "resourceTitleObject";
  public static final String resourceAltTitle = "resourceAltTitleObject";
  public static final String resourceAbstract = "resourceAbstractObject";
  public static final String resourceCredit = "resourceCreditObject";
  public static final String resourceIdentifier = "resourceIdentifier";
  public static final String resourceLanguage = "resourceLanguage";
  public static final String resourceDate = "resourceDate";
  public static final String org = "Org";
  public static final String orgForResource = "OrgForResource";
  public static final String organisationName = "organisationObject";
  public static final String recordLink = "recordLink";
  public static final String link = "link";
  public static final String coordinateSystem = "coordinateSystem";
  public static final String geom = "geom";
  public static final String location = "location";
  public static final String allKeywords = "allKeywords";
  public static final String tag = "tag";
  public static final String serviceType = "serviceType";
  public static final String format = "format";
  public static final String resourceTemporalExtentDateRange = "resourceTemporalExtentDateRange";
  public static final String resourceTemporalDateRange = "resourceTemporalDateRange";
  public static final String revisionYearForResource = "revisionYearForResource";
  public static final String revisionMonthForResource = "revisionMonthForResource";
  public static final String dateStamp = "dateStamp";
  public static final String resourceLineage = "lineageObject";
  public static final String specificationConformance = "specificationConformance";
  public static final String resolutionScaleDenominator = "resolutionScaleDenominator";

  public class Codelists {

    public static final String prefix = "cl_";
    public static final String characterSet = prefix + "characterSet";
    public static final String hierarchyLevel = prefix + "hierarchyLevel";
    public static final String status = prefix + "status";
    public static final String topic = prefix + "topic";
    public static final String maintenanceAndUpdateFrequency =
        prefix + "maintenanceAndUpdateFrequency";
  }

  public class CommonField {

    public static final String defaultText = "default";
    public static final String key = "key";
    public static final String link = "link";
  }

  public class LinkField {
    public static final String url = "urlObject";
    public static final String name = "nameObject";
    public static final String description = "descriptionObject";
  }
}
