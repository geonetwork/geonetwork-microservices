/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.common.xml;

import javax.xml.stream.XMLStreamWriter;
import net.sf.saxon.event.PipelineConfiguration;
import net.sf.saxon.event.Receiver;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.serialize.SerializationProperties;
import net.sf.saxon.stax.XMLStreamWriterDestination;

public class XmlStreamWriterDestinationInDocument extends XMLStreamWriterDestination {

  /**
   * Create an XMLStreamWriterDestination based on a supplied XMLStreamWriter.
   *
   * @param writer the supplied XmlStreamWriter
   */
  public XmlStreamWriterDestinationInDocument(XMLStreamWriter writer) {
    super(writer);
  }

  @Override
  public Receiver getReceiver(PipelineConfiguration pipe,
      SerializationProperties params) throws SaxonApiException {
    Receiver r = new ReceiverToXmlStreamWriteInDocument(getXMLStreamWriter());
    r.setPipelineConfiguration(pipe);
    return r;
  }

  @Override
  public void close() throws SaxonApiException {
    // Not my responsibility to close the writer.
  }
}
