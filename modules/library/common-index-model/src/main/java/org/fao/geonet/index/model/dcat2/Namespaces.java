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
  public static final Namespace DCAT =
      Namespace.getNamespace("dcat", DCAT_URI);

  static Map<String, String> URI_PREFIX_MAP = Map.ofEntries(
      new AbstractMap.SimpleEntry<String, String>(RDF_URI, RDF_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(DCT_URI, DCT_PREFIX),
      new AbstractMap.SimpleEntry<String, String>(SKOS_URI, SKOS_PREFIX)
  );
}
