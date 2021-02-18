/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

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
        @XmlNs(prefix = LOCN_PREFIX, namespaceURI = LOCN_URI),
        @XmlNs(prefix = VCARD_PREFIX, namespaceURI = VCARD_URI),
        @XmlNs(prefix = ADMS_PREFIX, namespaceURI = ADMS_URI)
    }
)
package org.fao.geonet.index.model.dcat2;

import static org.fao.geonet.index.model.dcat2.Namespaces.ADMS_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.ADMS_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.DCATAP_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.DCATAP_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.DCAT_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.DCAT_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.DCT_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.DCT_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.DC_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.DC_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.FOAF_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.FOAF_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.LOCN_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.LOCN_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.OWL_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.OWL_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.PROV_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.PROV_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.RDFS_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.RDFS_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.RDF_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.RDF_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.SKOS_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.SKOS_URI;
import static org.fao.geonet.index.model.dcat2.Namespaces.VCARD_PREFIX;
import static org.fao.geonet.index.model.dcat2.Namespaces.VCARD_URI;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlSchema;
