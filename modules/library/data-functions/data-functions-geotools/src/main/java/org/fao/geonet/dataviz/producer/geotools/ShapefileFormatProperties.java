package org.fao.geonet.dataviz.producer.geotools;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.Data;

/**
 * Global Shapefile protocol preferences
 */
@Data
public class ShapefileFormatProperties {

  /**
   * Optional - character set used to decode strings from the DBF file, if not
   * provided in DataQuery.encoding
   */
  Charset defaultCharset = StandardCharsets.UTF_8;
}
