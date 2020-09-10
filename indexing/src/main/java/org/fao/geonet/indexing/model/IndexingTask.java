package org.fao.geonet.indexing.model;

import lombok.Synchronized;
import org.fao.geonet.domain.Metadata;
import org.springframework.stereotype.Component;

public interface IndexingTask {
    void index(Metadata dbRecord);

    void reportError(String uuid, String message);
}
