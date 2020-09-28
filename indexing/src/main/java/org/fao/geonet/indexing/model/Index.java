package org.fao.geonet.indexing.model;

import java.nio.file.Path;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
