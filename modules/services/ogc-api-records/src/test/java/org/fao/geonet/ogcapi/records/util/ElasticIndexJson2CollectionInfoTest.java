/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */
package org.fao.geonet.ogcapi.records.util;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.fao.geonet.ogcapi.records.controller.model.CollectionInfo;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ElasticIndexJson2CollectionInfoTest {


    // sample Elastic Index JSON
    public static String sample1ElasticIndexJson;
    public static Map<String,Object> sample1ElasticIndexParsed;



  ElasticIndexJson2CollectionInfo elasticIndexJson2CollectionInfo = new ElasticIndexJson2CollectionInfo();


  @BeforeClass
  public static void  setupClass() throws IOException {




    sample1ElasticIndexJson = IOUtils.toString(
        ClassLoader.getSystemClassLoader().getResourceAsStream("org/fao/geonet/ogcapi/records/util/sampleCollectionServiceRecordElasticIndex.json"),
        "UTF-8");

     sample1ElasticIndexParsed = new ObjectMapper().readValue( sample1ElasticIndexJson,
         TypeFactory.mapType(HashMap.class, String.class, Object.class));
    sample1ElasticIndexParsed = (Map<String,Object> ) sample1ElasticIndexParsed.get("hits");
    sample1ElasticIndexParsed = (Map<String,Object> ) ((List)sample1ElasticIndexParsed.get("hits")).get(0);
    sample1ElasticIndexParsed = (Map<String,Object> )sample1ElasticIndexParsed.get("_source");


  }


  /**
   * Simple test case to read in the Elastic Index JSON, and parse it into a CollectionInfo.
   */
  @Test
  public void testFull() {
    var  collectionInfo = new CollectionInfo();
    elasticIndexJson2CollectionInfo.injectLinkedServiceRecordInfo(collectionInfo, sample1ElasticIndexParsed);

    assertEquals("catalog", collectionInfo.getType());
    assertEquals("record", collectionInfo.getItemType());

    assertEquals("GeoCat Demo OGCIAPI sub-portal", collectionInfo.getTitle());
    assertEquals("This is a sub-portal for testing OGCAPI.", collectionInfo.getDescription());

    assertEquals("http://www.opengis.net/def/crs/OGC/1.3/CRS84", collectionInfo.getCrs().get(0));
    assertEquals("2024-09-10T19:10:58.536961Z", collectionInfo.getCreated());
    assertEquals("2024-09-16T17:07:51.673194Z", collectionInfo.getUpdated());
    assertEquals("eng", collectionInfo.getLanguage().code);
    assertEquals("use limitation", collectionInfo.getLicense());

    //languages
    assertEquals(3, collectionInfo.getLanguages().size());
    assertEquals("dut", collectionInfo.getLanguages().get(0).code);
    assertEquals("spa", collectionInfo.getLanguages().get(1).code);
    assertEquals("eng", collectionInfo.getLanguages().get(2).code);

    //keywords
    assertEquals(6, collectionInfo.getKeywords().size());
    assertEquals("GEONETWORK", collectionInfo.getKeywords().get(0));
    assertEquals("OSGeo", collectionInfo.getKeywords().get(1));
    assertEquals("GeoCat", collectionInfo.getKeywords().get(2));
    assertEquals("OGCAPI", collectionInfo.getKeywords().get(3));
    assertEquals("Algeria", collectionInfo.getKeywords().get(4));
    assertEquals("Antarctica", collectionInfo.getKeywords().get(5));


    //themes
    assertEquals(2, collectionInfo.getThemes().size());
    //first theme
    assertEquals("otherKeywords-theme", collectionInfo.getThemes().get(0).schema);
    assertEquals(4, collectionInfo.getThemes().get(0).getConcepts().size());
    assertEquals("GEONETWORK", collectionInfo.getThemes().get(0).getConcepts().get(0).id);
    assertEquals("OSGeo", collectionInfo.getThemes().get(0).getConcepts().get(1).id);
    assertEquals("GeoCat", collectionInfo.getThemes().get(0).getConcepts().get(2).id);
    assertEquals("OGCAPI", collectionInfo.getThemes().get(0).getConcepts().get(3).id);

    //2nd theme
    assertEquals("http://localhost:8080/geonetwork/srv/api/registries/vocabularies/external.place.regions", collectionInfo.getThemes().get(1).schema);
    assertEquals(2, collectionInfo.getThemes().get(1).getConcepts().size());
    assertEquals("Algeria", collectionInfo.getThemes().get(1).getConcepts().get(0).id);
    assertEquals("Antarctica", collectionInfo.getThemes().get(1).getConcepts().get(1).id);

    //contacts
    assertEquals(1, collectionInfo.getContacts().size());
    assertEquals("Jody Garnett", collectionInfo.getContacts().get(0).getName());
    assertEquals("Hat Wearer Extraordinaire", collectionInfo.getContacts().get(0).getPosition());
    assertEquals("GeoCat Canada Ltd", collectionInfo.getContacts().get(0).getOrganization());
    //phones
    assertEquals(1, collectionInfo.getContacts().get(0).getPhones().size());
    assertEquals("+1 (250) 213-1219", collectionInfo.getContacts().get(0).getPhones().get(0).getValue());
    //email
    assertEquals(1, collectionInfo.getContacts().get(0).getEmails().size());
    assertEquals("jody.garnett@geocat.net", collectionInfo.getContacts().get(0).getEmails().get(0).getValue());
    //address
    assertEquals(1, collectionInfo.getContacts().get(0).getAddresses().size());
    assertEquals(1, collectionInfo.getContacts().get(0).getAddresses().get(0).getDeliveryPoint().size());
    assertEquals("3613 Doncaster Drr, Victoria, BC, V8P 3W5, Canada", collectionInfo.getContacts().get(0).getAddresses().get(0).getDeliveryPoint().get(0));

    //extent - spatial
    assertEquals("http://www.opengis.net/def/crs/OGC/1.3/CRS84", collectionInfo.getExtent().getSpatial().getCrs());
    assertEquals(4, collectionInfo.getExtent().getSpatial().getBbox().size());
    assertEquals(-180.0, collectionInfo.getExtent().getSpatial().getBbox().get(0).doubleValue() ,0);
    assertEquals(-90.0, collectionInfo.getExtent().getSpatial().getBbox().get(1).doubleValue() ,0);
    assertEquals(180.0, collectionInfo.getExtent().getSpatial().getBbox().get(2).doubleValue() ,0);
    assertEquals(90.0, collectionInfo.getExtent().getSpatial().getBbox().get(3).doubleValue() ,0);
    //extent - temporal
    assertEquals("http://www.opengis.net/def/uom/ISO-8601/0/Gregorian", collectionInfo.getExtent().getTemporal().getTrs());
    assertEquals(2, collectionInfo.getExtent().getTemporal().getInterval().size());
    assertEquals("2016-02-29T20:00:00.000Z", collectionInfo.getExtent().getTemporal().getInterval().get(0));
    assertEquals("2016-02-29T20:00:00.000Z", collectionInfo.getExtent().getTemporal().getInterval().get(1));

  }
}
