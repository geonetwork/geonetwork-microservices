package org.fao.geonet.indexing.model;

import org.fao.geonet.domain.Metadata;

import java.util.Map;

public class IndexRecord extends IndexDocument {
    private String resourceTitle;
    private String resourceAbstract;
    private Map<String, String> otherProperties;

    IndexRecord IndexRecord(Metadata dbRecord) {
        return this;
    }
}
