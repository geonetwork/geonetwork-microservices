package org.fao.geonet.common.search;

import org.springframework.http.MediaType;
import org.springframework.util.MimeType;

public class GnMediaType extends MimeType {

  public GnMediaType(String type) {
    super(type);
  }

  public static final String APPLICATION_DCAT2_XML_VALUE = "application/dcat2+xml";
  public static final MediaType APPLICATION_DCAT2_XML;

  public static final String APPLICATION_ISO19139_XML_VALUE = "application/iso19139+xml";
  public static final MediaType APPLICATION_ISO19139_XML;

  public static final String APPLICATION_ISO19115_3_XML_VALUE = "application/iso19115-3+xml";
  public static final MediaType APPLICATION_ISO19115_3_XML;

  public static final String APPLICATION_GN_XML_VALUE = "application/gn+xml";
  public static final MediaType APPLICATION_GN_XML;

  public static final String APPLICATION_JSON_LD_VALUE = "application/ld+json";
  public static final MediaType APPLICATION_JSON_LD;

  static {
    APPLICATION_DCAT2_XML = new MediaType("application", "dcat2+xml");
    APPLICATION_ISO19139_XML = new MediaType("application", "iso19139+xml");
    APPLICATION_ISO19115_3_XML = new MediaType("application", "iso19115-3+xml");
    APPLICATION_GN_XML = new MediaType("application", "gn+xml");
    APPLICATION_JSON_LD = new MediaType("application", "ld+json");
  }
}
