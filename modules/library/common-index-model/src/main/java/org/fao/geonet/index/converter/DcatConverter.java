/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.index.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.fao.geonet.index.model.gn.Contact;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames.Codelists;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames.CommonField;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.geojson.GeoJsonReader;

/**
 * Index document to DCAT mapping.
 *
 */
public class DcatConverter {

  public enum Types {
    Dataset,
    DataFeed,
    Organization,
    ContactPoint,
    Distribution,
    DataDownload,
    AggregateRating,
    ImageObject,
    GeoShape,
    Url;
  }

  private static ObjectMapper mapper = new ObjectMapper();

  /**
   * Convert an index document into a JSON LD document.
   */
  public static ObjectNode convert(IndexRecord record) {

    return null;
  }

}
