package org.fao.geonet.common;

import javax.xml.stream.XMLStreamWriter;
import net.sf.saxon.stax.ReceiverToXMLStreamWriter;
import net.sf.saxon.trans.XPathException;

public class ReceiverToXMLStreamWriteWithDocument  extends ReceiverToXMLStreamWriter {
  public ReceiverToXMLStreamWriteWithDocument(XMLStreamWriter writer) {
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
}
