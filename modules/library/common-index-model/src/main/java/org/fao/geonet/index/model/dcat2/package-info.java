@XmlSchema(
    xmlns = {
        @XmlNs(prefix = SKOS_PREFIX, namespaceURI = SKOS_URI),
        @XmlNs(prefix = RDF_PREFIX, namespaceURI = RDF_URI),
        @XmlNs(prefix = DCAT_PREFIX, namespaceURI = DCAT_URI)
    }
)
package org.fao.geonet.index.model.dcat2;

import static org.fao.geonet.index.model.dcat2.Namespaces.DCAT_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.DCAT_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.RDF_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.RDF_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.SKOS_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.SKOS_URI;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;