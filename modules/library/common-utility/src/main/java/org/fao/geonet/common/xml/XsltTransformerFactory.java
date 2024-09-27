package org.fao.geonet.common.xml;

import javax.xml.transform.stream.StreamSource;
import lombok.extern.slf4j.Slf4j;
import net.sf.saxon.Configuration;
import net.sf.saxon.TransformerFactoryImpl;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.trans.XPathException;

/**
 * Setup Saxon transformer with configuration which register Java function extensions.
 */
@Slf4j(topic = "org.fao.geonet.common")
public class XsltTransformerFactory {

  public static final String SAXON_CONFIGURATION_PATH = "/saxon-configuration.xml";
  public static final String CHECK_SAXON_CONFIGURATION = "Check saxon configuration. Error is: {}";

  private static TransformerFactoryImpl factory;
  private static Processor processor;

  /**
   * Get configured Saxon transformer factory.
   */
  public static TransformerFactoryImpl get() {
    if (factory != null) {
      return factory;
    }
    try {
      factory = new net.sf.saxon.TransformerFactoryImpl(getConfiguration());
    } catch (XPathException e) {
      log.error(CHECK_SAXON_CONFIGURATION, e.getMessage());
      factory = new TransformerFactoryImpl();
    }
    return factory;

  }

  /**
   * Get configured Saxon processor.
   */
  public static Processor getProcessor() {
    if (processor != null) {
      return processor;
    }
    try {
      processor = new Processor(getConfiguration());
    } catch (XPathException e) {
      log.error(CHECK_SAXON_CONFIGURATION, e.getMessage());
      processor = new Processor(false);
    }
    return processor;
  }

  private static Configuration getConfiguration() throws XPathException {
    return Configuration
        .readConfiguration(new StreamSource(
            XsltTransformerFactory.class.getResourceAsStream(SAXON_CONFIGURATION_PATH)));
  }
}
