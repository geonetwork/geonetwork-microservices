@XmlSchema(
    xmlns = {
        @XmlNs(prefix = SKOS_PREFIX, namespaceURI = SKOS_URI),
        @XmlNs(prefix = RDF_PREFIX, namespaceURI = RDF_URI),
        @XmlNs(prefix = RDFS_PREFIX, namespaceURI = RDFS_URI),
        @XmlNs(prefix = DCAT_PREFIX, namespaceURI = DCAT_URI),
        @XmlNs(prefix = DCATAP_PREFIX, namespaceURI = DCATAP_URI),
        @XmlNs(prefix = PROV_PREFIX, namespaceURI = PROV_URI),
        @XmlNs(prefix = OWL_PREFIX, namespaceURI = OWL_URI),
        @XmlNs(prefix = DC_PREFIX, namespaceURI = DC_URI),
        @XmlNs(prefix = DCT_PREFIX, namespaceURI = DCT_URI),
        @XmlNs(prefix = FOAF_PREFIX, namespaceURI = FOAF_URI),
        @XmlNs(prefix = VCARD_PREFIX, namespaceURI = VCARD_URI),
        @XmlNs(prefix = ADMS_PREFIX, namespaceURI = ADMS_URI)
    }
)
package org.fao.geonet.index.model.dcat2;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;

import static org.fao.geonet.index.model.dcat2.Namespaces.*;
