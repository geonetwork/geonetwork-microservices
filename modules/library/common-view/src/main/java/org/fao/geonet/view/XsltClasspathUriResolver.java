/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.view;

import java.io.IOException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import net.sf.saxon.lib.StandardURIResolver;
import net.sf.saxon.trans.XPathException;
import org.springframework.core.io.ClassPathResource;

/**
 * Resolve resource in XSLT files.
 *
 * <p>
 * Resource can be relative to the XSLT (and a default Saxon resolver will be used or in the
 * classpath. To load a resource from the classpath use:
 *
 * <pre>
 *  <xsl:import href="classpath:xslt/core/commons/xsl-params-core.xsl"/>
 * </pre>
 * </p>
 */
public class XsltClasspathUriResolver extends StandardURIResolver {

  public static final String CLASSPATH_PREFIX = "classpath:";

  @Override
  public Source resolve(String href, String base) throws XPathException {
    if (href.startsWith(CLASSPATH_PREFIX)) {
      return loadResourceFromClasspath(href, true);
    } else {
      return super.resolve(href, adaptClasspathBasePath(base));
    }
  }

  private String adaptClasspathBasePath(String base) {
    if (base.startsWith(CLASSPATH_PREFIX)) {
      ClassPathResource pathResource = new ClassPathResource(base.replace(CLASSPATH_PREFIX, ""));
      if (pathResource.exists()) {
        try {
          base = pathResource.getFile().getCanonicalPath();
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
      }
    }
    return base;
  }

  private StreamSource loadResourceFromClasspath(String path, boolean tryReplacingPrefix) {
    ClassPathResource resource = new ClassPathResource(path);
    if (resource.exists()) {
      try {
        return new StreamSource(resource.getInputStream());
      } catch (IOException ioException) {
        System.out.println(String.format(
            "ClasspathResourceURIResolver can't find '%s'. IOException.",
            path
        ));
        return null;
      }
    } else if (tryReplacingPrefix) {
      return null; //loadResourceFromClasspath(path.replace(CLASSPATH_PREFIX, ""), false);
    }
    return null;
  }
}
