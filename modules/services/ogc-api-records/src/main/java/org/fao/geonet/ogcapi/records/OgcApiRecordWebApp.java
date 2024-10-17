/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records;

import org.fao.geonet.index.converter.GeoJsonConverter;
import org.fao.geonet.ogcapi.records.controller.CapabilitiesApiController;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;


/**
 * We remove the "GeoJsonConverter" from the scanned components so this will app will use the
 * "OgcApiGeoJsonConverter".
 */
@SpringBootApplication
@Import({CapabilitiesApiController.class})
@ComponentScan(
    value = {"org.fao.geonet", "org.fao.geonet.domain"}, excludeFilters =
    {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = GeoJsonConverter.class)}
)
//@ComponentScan(
//    value = {"org.fao.geonet", "org.fao.geonet.domain"}
//)
@Configuration
@EnableCaching
public class OgcApiRecordWebApp extends SpringBootServletInitializer {


}