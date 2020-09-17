package org.fao.geonet.batch;

import java.util.List;
import org.springframework.batch.item.ItemWriter;

public class NoOpItemWriter implements ItemWriter<Object> {
  @Override
  public void write(List<? extends Object> items) throws Exception {
  }
}