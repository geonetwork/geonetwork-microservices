/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.searching.domain;

/**
 * The list of reserved groups. Ids and names are hardcoded and have special meaning in Geonetwork.
 *
 */
public enum ReservedGroup {
  /**
   * The "All" group.  IE the group that represents all.
   */
  all(1),
  /**
   * The Intranet group.  IE the group that represents all users within the same intranet as the
   * geonetwork server.
   */
  intranet(0),
  /**
   * The "Guest" group.  IE the group representing all users not signed in.
   */
  guest(-1);

  // Not final so Tests can change id
  private int id;

  private ReservedGroup(int id) {
    this.id = id;
  }

  /**
   * Get the id of the reserved group.
   *
   * @return the id of the reserved group.
   */
  public int getId() {
    return id;
  }

}
