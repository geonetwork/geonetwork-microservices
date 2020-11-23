/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.errors;

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

@Component
public class GeoNetworkErrorAttributes extends DefaultErrorAttributes {

  @Override
  public Map<String, Object> getErrorAttributes(
      WebRequest webRequest, ErrorAttributeOptions options) {
    Map<String, Object> errorAttributes =
        super.getErrorAttributes(webRequest, options);
    errorAttributes.put("locale", webRequest.getLocale()
        .toString());
    //errorAttributes.remove("error");

    //...
    // overriding the Whitelabel Page

    return errorAttributes;
  }
}

