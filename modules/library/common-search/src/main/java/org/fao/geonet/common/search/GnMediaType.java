/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.common.search;

import org.springframework.http.MediaType;
import org.springframework.util.MimeType;

public class GnMediaType extends MimeType {


  public GnMediaType(String type) {
    super(type);
  }

  public static final MediaType TEXT_TURTLE;
  public static final String TEXT_TURTLE_VALUE = "text/turtle";

  public static final String APPLICATION_DCAT2_XML_VALUE = "application/dcat2+xml";
  public static final MediaType APPLICATION_DCAT2_XML;

  public static final String APPLICATION_ISO19139_XML_VALUE = "application/iso19139+xml";
  public static final MediaType APPLICATION_ISO19139_XML;

  public static final String APPLICATION_OPENSEARCH_XML_VALUE =
      "application/opensearchdescription+xml";
  public static final MediaType APPLICATION_OPENSEARCH_XML;

  public static final String APPLICATION_RDF_XML_VALUE = "application/rdf+xml";
  public static final MediaType APPLICATION_RDF_XML;

  public static final String APPLICATION_ISO19115_3_XML_VALUE = "application/iso19115-3+xml";
  public static final MediaType APPLICATION_ISO19115_3_XML;

  public static final String APPLICATION_GN_XML_VALUE = "application/gn+xml";
  public static final MediaType APPLICATION_GN_XML;

  public static final String APPLICATION_JSON_LD_VALUE = "application/ld+json";
  public static final MediaType APPLICATION_JSON_LD;

  public static final String APPLICATION_GEOJSON_VALUE = "application/geo+json";
  public static final MediaType APPLICATION_GEOJSON;

  public static final String APPLICATION_ELASTICJSON_VALUE = "application/gnindex+json";
  public static final MediaType APPLICATION_ELASTICJSON;


  static {
    TEXT_TURTLE = new MediaType("text", "turtle");

    APPLICATION_DCAT2_XML = new MediaType("application", "dcat2+xml");
    APPLICATION_RDF_XML = new MediaType("application", "rdf+xml");
    APPLICATION_OPENSEARCH_XML = new MediaType("application", "opensearchdescription+xml");
    APPLICATION_ISO19139_XML = new MediaType("application", "iso19139+xml");
    APPLICATION_ISO19115_3_XML = new MediaType("application", "iso19115-3+xml");
    APPLICATION_GN_XML = new MediaType("application", "gn+xml");
    APPLICATION_JSON_LD = new MediaType("application", "ld+json");
    APPLICATION_GEOJSON = new MediaType("application", "geo+json");
    APPLICATION_ELASTICJSON = new MediaType("application", "gnindex+json");
  }
}
