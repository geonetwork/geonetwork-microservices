/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */


package org.fao.geonet.index.model.opensearch;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each Java content interface and Java element interface
 * generated in the com.a9.__.spec.opensearch._1 package.
 *
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation for XML content. The Java representation of
 * XML content can consist of schema derived interfaces and classes representing the binding of
 * schema type definitions, element declarations and model groups.  Factory methods for each of
 * these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {


  /**
   * Create a new ObjectFactory that can be used to create new instances of schema derived classes
   * for package: com.a9.__.spec.opensearch._1.
   */
  public ObjectFactory() {
  }

  /**
   * Create an instance of {@link OpenSearchDescription }.
   */
  public OpenSearchDescription createOpenSearchDescription() {
    return new OpenSearchDescription();
  }

  /**
   * Create an instance of {@link OpenSearchDescription.Url }.
   */
  public OpenSearchDescription.Url createOpenSearchDescriptionUrl() {
    return new OpenSearchDescription.Url();
  }

  /**
   * Create an instance of {@link OpenSearchDescription.Image }.
   */
  public OpenSearchDescription.Image createOpenSearchDescriptionImage() {
    return new OpenSearchDescription.Image();
  }

  /**
   * Create an instance of {@link OpenSearchDescription.Query }.
   */
  public OpenSearchDescription.Query createOpenSearchDescriptionQuery() {
    return new OpenSearchDescription.Query();
  }

}
