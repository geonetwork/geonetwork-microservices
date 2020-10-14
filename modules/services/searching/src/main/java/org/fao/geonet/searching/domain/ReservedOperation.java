/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.searching.domain;

/**
 * The system reserved operations. Ids and names are hardcoded and have special meaning in
 * Geonetwork.
 *
 */
public enum ReservedOperation {
  /**
   * The operation required to view the metadata.
   */
  view(0),
  /**
   * The operation required to download the metadata.
   */
  download(1),
  /**
   * The operation required to edit the metadata.
   */
  editing(2),
  /**
   * The operation required for listeners to be notified of changes about the metadata.
   */
  notify(3),
  /**
   * Identifies a metadata as having a "dynamic" component.
   */
  dynamic(5),
  /**
   * Operation that allows the metadata to be one of the featured metadata.
   */
  featured(6);

  // Not final so Tests can change id
  private int id;

  private ReservedOperation(int id) {
    this.id = id;
  }

  /**
   * Get the id of the operation.
   *
   * @return the id of the operation.
   */
  public int getId() {
    return id;
  }

}
