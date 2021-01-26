/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.ogcapi.records;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.fao.geonet.common.search.SearchConfiguration.Format;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "gn.ogcapi")
public class OgcApiConfiguration {

  List<Format> formats = new ArrayList<>();
}