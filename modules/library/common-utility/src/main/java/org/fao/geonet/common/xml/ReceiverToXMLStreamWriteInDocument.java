/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.common.xml;

import javax.xml.stream.XMLStreamWriter;
import net.sf.saxon.stax.ReceiverToXMLStreamWriter;
import net.sf.saxon.trans.XPathException;

public class ReceiverToXMLStreamWriteInDocument extends ReceiverToXMLStreamWriter {
  public ReceiverToXMLStreamWriteInDocument(XMLStreamWriter writer) {
    super(writer);
  }

  @Override
  public void startDocument(int properties) throws XPathException {
    // Document exists.
  }

  @Override
  public void endDocument() throws XPathException {
    // Document exists.
  }

  @Override
  public void close() throws XPathException {
    //
  }
}
