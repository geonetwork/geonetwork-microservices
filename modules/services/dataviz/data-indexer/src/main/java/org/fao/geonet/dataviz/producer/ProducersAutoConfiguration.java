package org.fao.geonet.dataviz.producer;

import org.fao.geonet.dataviz.producer.geotools.GeoPackageFormat;
import org.fao.geonet.dataviz.producer.geotools.GeoPackageFormatProperties;
import org.fao.geonet.dataviz.producer.geotools.GeoToolsDataReader;
import org.fao.geonet.dataviz.producer.geotools.ShapefileFormat;
import org.fao.geonet.dataviz.producer.geotools.ShapefileFormatProperties;
import org.fao.geonet.dataviz.producer.geotools.WfsFormat;
import org.fao.geonet.dataviz.producer.geotools.WfsFormatProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducersAutoConfiguration {

  public @Bean Producers producers() {
    return new Producers();
  }

  @ConfigurationProperties(prefix = "dataviz.indexer.producers.shp")
  public @Bean ShapefileFormatProperties geotoolsShapefileFormatProperties() {
    return new ShapefileFormatProperties();
  }

  public @Bean GeoToolsDataReader geoToolsDataReader() {
    return new GeoToolsDataReader();
  }

  public @Bean ShapefileFormat geotoolsShapefileFormat() {
    return new ShapefileFormat();
  }

  @ConfigurationProperties(prefix = "dataviz.indexer.producers.wfs")
  public @Bean WfsFormatProperties geotoolsWfsFormatProperties() {
    return new WfsFormatProperties();
  }

  public @Bean WfsFormat geotoolsWfsFormat() {
    return new WfsFormat();
  }

  // Disabled, results in a java.lang.StackOverflowError at current 25.x
  // GeoTools version
  // public @Bean GeoJsonFormat geotoolsGeoJsonFormat() {
  // return new GeoJsonFormat();
  // }

  @ConfigurationProperties(prefix = "dataviz.indexer.producers.geopackage")
  public @Bean GeoPackageFormatProperties geotoolsGeoPackageFormatProperties() {
    return new GeoPackageFormatProperties();
  }

  public @Bean GeoPackageFormat geotoolsGeoPackageFormat() {
    return new GeoPackageFormat();
  }
}
