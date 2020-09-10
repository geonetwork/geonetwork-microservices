package org.fao.geonet.indexing.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.nio.file.Path;

@Getter
@Setter
@ToString
public class Index {
    private String name;
    private IndexType type;

    Path getConfigFile() {

        return null;
    }
}
