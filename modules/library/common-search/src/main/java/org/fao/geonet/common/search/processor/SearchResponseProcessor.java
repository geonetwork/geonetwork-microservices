/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.common.search.processor;

import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpSession;
import org.fao.geonet.common.search.domain.UserInfo;

public interface SearchResponseProcessor {
  /**
   * Receive Elasticsearch response, process the response
   * and stream response to client.
   */
  void processResponse(HttpSession httpSession,
      InputStream streamFromServer, OutputStream streamToClient,
      UserInfo userInfo, String bucket, Boolean addPermissions) throws Exception;

  default void setTransformation(String acceptHeader) {
  }
}
