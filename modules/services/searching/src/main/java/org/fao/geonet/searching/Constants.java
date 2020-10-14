/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.searching;

public final class Constants {

  public static final String ENCODING = System.getProperty("geonetwork.file.encoding", "UTF-8");

  public static final class IndexFieldNames {
    public static final String UUID = "uuid";
    public static final String OWNER = "owner";
    public static final String GROUP_OWNER = "groupOwner";
    public static final String OP_PREFIX = "op";
  }

  public static final class Elem {
    public static final String SELECTED = "selected";
    public static final String IS_PUBLISHED_TO_ALL = "isPublishedToAll";
    public static final String GUEST_DOWNLOAD = "guestdownload";
    public static final String EDIT = "edit";
    public static final String OWNER = "owner";
  }

  public static final class Selection {
    public static final String DEFAULT_SELECTION_METADATA_BUCKET = "metadata";
  }

  public static final class NodeInfo {
    public static final String DEFAULT_NODE = "srv";
  }

}
