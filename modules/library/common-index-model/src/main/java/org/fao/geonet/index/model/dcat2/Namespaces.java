/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.model.dcat2;

import java.util.AbstractMap;
import java.util.Map;
import org.jdom.Namespace;

public class Namespaces {

  public static final String RDF_PREFIX = "rdf";
  public static final String RDF_URI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
  public static final String DCT_PREFIX = "dct";
  public static final String DCT_URI = "http://purl.org/dc/terms/";
  public static final String SKOS_PREFIX = "skos";
  public static final String SKOS_URI = "http://www.w3.org/2004/02/skos/core#";
  public static final String DCAT_PREFIX = "dcat";
  public static final String DCAT_URI = "http://www.w3.org/ns/dcat#";
  public static final String DCATAP_PREFIX = "dcatap";
  public static final String DCATAP_URI = "http://data.europa.eu/r5r/";
  public static final String GEODCATAP_PREFIX = "geodcatap";
  public static final String GEODCATAP_URI = "http://data.europa.eu/930/";
  public static final String ADMS_PREFIX = "adms";
  public static final String ADMS_URI = "http://www.w3.org/ns/adms#";
  public static final String CNT_PREFIX = "cnt";
  public static final String CNT_URI = "http://www.w3.org/2011/content#";
  public static final String DC_PREFIX = "dc";
  public static final String DC_URI = "http://purl.org/dc/elements/1.1/";
  public static final String DCTYPE_PREFIX = "dctype";
  public static final String DCTYPE_URI = "http://purl.org/dc/dcmitype/";
  public static final String EARL_PREFIX = "earl";
  public static final String EARL_URI = "http://www.w3.org/ns/earl#";
  public static final String FOAF_PREFIX = "foaf";
  public static final String FOAF_URI = "http://xmlns.com/foaf/0.1/";
  public static final String GML_PREFIX = "gml";
  public static final String GML_URI = "http://www.opengis.net/gml";
  public static final String GSP_PREFIX = "gsp";
  public static final String GSP_URI = "http://www.opengis.net/ont/geosparql#";
  public static final String INSPIRE_PREFIX = "inspire";
  public static final String INSPIRE_URI = "http://inspire.ec.europa.eu/schemas/common/1.0";
  public static final String INSPIREGEO_PREFIX = "inspire-geoportal";
  public static final String INSPIREGEO_URI = "http://inspire.ec.europa.eu/schemas/geoportal/1.0";
  public static final String LOCN_PREFIX = "locn";
  public static final String LOCN_URI = "http://www.w3.org/ns/locn#";
  public static final String OWL_PREFIX = "owl";
  public static final String OWL_URI = "http://www.w3.org/2002/07/owl#";
  public static final String ORG_PREFIX = "org";
  public static final String ORG_URI = "http://www.w3.org/ns/org#";
  public static final String PROV_PREFIX = "prov";
  public static final String PROV_URI = "http://www.w3.org/ns/prov#";
  public static final String RDFS_PREFIX = "rdfs";
  public static final String RDFS_URI = "http://www.w3.org/2000/01/rdf-schema#";
  public static final String SCHEMA_PREFIX = "schema";
  public static final String SCHEMA_URI = "http://schema.org/";
  public static final String SRV_PREFIX = "srv";
  public static final String SRV_URI = "http://www.isotc211.org/2005/srv";
  public static final String VCARD_PREFIX = "vcard";
  public static final String VCARD_URI = "http://www.w3.org/2006/vcard/ns#";
  public static final String WDRS_PREFIX = "wdrs";
  public static final String WDRS_URI = "http://www.w3.org/2007/05/powder-s#";
  public static final String SPDX_PREFIX = "spdx";
  public static final String SPDX_URI = "http://spdx.org/rdf/terms#";
  public static final String XLINK_PREFIX = "xlink";
  public static final String XLINK_URI = "http://www.w3.org/1999/xlink";

  public static final Namespace DCAT = Namespace.getNamespace("dcat", DCAT_URI);

  static Map<String, String> URI_PREFIX_MAP = Map.ofEntries(
      new AbstractMap.SimpleEntry<String, String>(RDF_URI, RDF_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(RDFS_URI, RDFS_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(OWL_URI, OWL_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(DCT_URI, DCT_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(SKOS_URI, SKOS_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(LOCN_URI, LOCN_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(XLINK_URI, XLINK_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(VCARD_URI, VCARD_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(PROV_URI, PROV_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(SCHEMA_URI, SCHEMA_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(ORG_URI, ORG_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(SRV_URI, SRV_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(WDRS_URI, WDRS_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(ADMS_URI, ADMS_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(CNT_URI, CNT_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(DC_URI, DC_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(DCAT_URI, DCAT_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(DCATAP_URI, DCATAP_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(GEODCATAP_URI, GEODCATAP_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(DCT_URI, DCT_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(DCTYPE_URI, DCTYPE_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(EARL_URI, EARL_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(FOAF_URI, FOAF_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(GML_URI, GML_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(GSP_URI, GSP_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(INSPIRE_URI, INSPIRE_PREFIX)
  );
}
