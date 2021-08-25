package org.fao.geonet.dataviz.producer.geotools;

import static org.geotools.data.wfs.impl.WFSDataAccessFactory.LENIENT;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.MAX_CONNECTION_POOL_SIZE;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.PROTOCOL;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.TIMEOUT;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.TRY_GZIP;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.URL;
import static org.geotools.data.wfs.impl.WFSDataAccessFactory.USE_HTTP_CONNECTION_POOLING;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.fao.geonet.dataviz.model.DataQuery;
import org.fao.geonet.dataviz.model.DataSource;

public class WfsFormat extends GeoToolsFormat {

  private @Getter @Setter WfsFormatProperties defaults = new WfsFormatProperties();

  private final @Getter String name = "OGC WFS";

  @Override
  public boolean canHandle(@NonNull URI datasetUri) {
    final String scheme = datasetUri.getScheme();
    if (!("http".equals(scheme) || "https".equals(scheme))) {
      return false;
    }
    Map<String, String> queryParams = URLEncodedUtils.parse(datasetUri, StandardCharsets.UTF_8)
        .stream()
        .collect(Collectors.toMap(nvp -> nvp.getName().toUpperCase(), NameValuePair::getValue));
    boolean isWFS = "WFS".equalsIgnoreCase(queryParams.get("SERVICE"));
    boolean isGetCaps = "GETCAPABILITIES".equalsIgnoreCase(queryParams.get("REQUEST"));
    if (isWFS && (isGetCaps || !queryParams.containsKey("REQUEST"))) {
      return true;
    }
    // heuristic for a URL that does not specify the (mandatory) OWS SERVICE request
    // parameter, and either specifies request=getcapabilities, or does not set a
    // request parameter at all. REVISIT: make it a WfsFormatProperties option?
    if (!queryParams.containsKey("SERVICE") && (isGetCaps || !queryParams.containsKey("REQUEST"))) {
      String path = datasetUri.getPath();
      String endPoint = Paths.get(path).getFileName().toString().toLowerCase();
      return endPoint.equals("wfs");
    }
    return false;
  }

  @Override
  protected Map<String, ?> toConnectionParams(@NonNull DataQuery query) {
    final DataSource source = query.getSource();
    if (!canHandle(source.getUri())) {
      throw new IllegalArgumentException(
          "Query URI not recognized as a WFS GetCapabilities request");
    }

    final URL getCapabilitiesUrl;
    try {
      getCapabilitiesUrl = source.getUri().toURL();
    } catch (MalformedURLException e) {
      throw new IllegalArgumentException(e);
    }

    final boolean httpConnectionPooling = defaults.isHttpConnectionPooling();
    final int httpConnectionPoolSize = defaults.getHttpConnectionPoolSize();
    final Boolean preferPost = defaults.getPreferPost();
    final boolean lenient = defaults.isLenient();
    final boolean tryGzip = defaults.isTryGzip();
    final Integer timeout = defaults.getTimeout();

    Map<String, Object> params = new HashMap<>();
    params.put(URL.key, getCapabilitiesUrl);
    if (null != preferPost) {
      params.put(PROTOCOL.key, preferPost);
    }
    if (httpConnectionPooling) {
      params.put(USE_HTTP_CONNECTION_POOLING.key, httpConnectionPooling);
      if (httpConnectionPoolSize > 0) {
        params.put(MAX_CONNECTION_POOL_SIZE.key, httpConnectionPoolSize);
      }
    }
    params.put(LENIENT.key, lenient);
    params.put(TRY_GZIP.key, tryGzip);
    if (timeout != null) {
      params.put(TIMEOUT.key, timeout);
    }
    return params;
  }

}
