package org.fao.geonet.dataviz.producer;

import java.util.List;
import org.fao.geonet.dataviz.producer.geotools.GeoPackageFormat;
import org.fao.geonet.dataviz.producer.geotools.GeoPackageFormatProperties;
import org.fao.geonet.dataviz.producer.geotools.GeoToolsDataReader;
import org.fao.geonet.dataviz.producer.geotools.GeoToolsFormat;
import org.fao.geonet.dataviz.producer.geotools.ShapefileFormat;
import org.fao.geonet.dataviz.producer.geotools.ShapefileFormatProperties;
import org.fao.geonet.dataviz.producer.geotools.WfsFormat;
import org.fao.geonet.dataviz.producer.geotools.WfsFormatProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducersAutoConfiguration {

  private @Autowired List<GeoToolsFormat> supportedFormats;
  private @Autowired List<DatasetReader> readers;

  public @Bean Producers producers() {
    return new Producers(readers);
  }

  public @Bean GeoToolsDataReader geoToolsDataReader() {
    GeoToolsDataReader geoToolsDataReader = new GeoToolsDataReader();
    geoToolsDataReader.setSupportedFormats(supportedFormats);
    return geoToolsDataReader;
  }

  @ConfigurationProperties(prefix = "dataviz.indexer.producers.shp")
  public @Bean ShapefileFormatProperties geotoolsShapefileFormatProperties() {
    return new ShapefileFormatProperties();
  }

  public @Bean ShapefileFormat geotoolsShapefileFormat() {
    ShapefileFormat shapefileFormat = new ShapefileFormat();
    shapefileFormat.setDefaults(geotoolsShapefileFormatProperties());
    return shapefileFormat;
  }

  @ConfigurationProperties(prefix = "dataviz.indexer.producers.wfs")
  public @Bean WfsFormatProperties geotoolsWfsFormatProperties() {
    return new WfsFormatProperties();
  }

  public @Bean WfsFormat geotoolsWfsFormat() {
    WfsFormat wfsFormat = new WfsFormat();
    wfsFormat.setDefaults(geotoolsWfsFormatProperties());
    return wfsFormat;
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
    GeoPackageFormat geoPackageFormat = new GeoPackageFormat();
    geoPackageFormat.setDefaults(geotoolsGeoPackageFormatProperties());
    return geoPackageFormat;
  }
}
