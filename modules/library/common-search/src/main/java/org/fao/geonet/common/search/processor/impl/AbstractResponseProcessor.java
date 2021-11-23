package org.fao.geonet.common.search.processor.impl;

import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import org.fao.geonet.common.search.processor.SearchResponseProcessor;

public abstract class AbstractResponseProcessor implements SearchResponseProcessor {
  protected JsonParser parserForStream(InputStream streamFromServer) throws IOException {
    JsonParser parser = ResponseParser.jsonFactory.createParser(streamFromServer);
    parser.nextToken();
    return parser;
  }

}
