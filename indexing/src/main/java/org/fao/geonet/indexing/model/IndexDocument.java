package org.fao.geonet.indexing.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class IndexDocument {
  private String id;
}
