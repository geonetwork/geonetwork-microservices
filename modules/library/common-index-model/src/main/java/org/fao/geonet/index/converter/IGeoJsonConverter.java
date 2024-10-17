package org.fao.geonet.index.converter;

import org.fao.geonet.index.model.gn.IndexRecord;

public interface IGeoJsonConverter {
  Object convert(IndexRecord record);
}
