package org.fao.geonet.ogcapi.records.service;

import org.fao.geonet.domain.Source;
import org.fao.geonet.domain.SourceType;
import org.fao.geonet.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Nullable;
import java.util.List;

@Service
public class CollectionService {
  @Autowired
  private SourceRepository sourceRepository;

  public boolean existsCollection(String collectionId) {
    Source source = retrieveSourceForCollection(collectionId);

    return (source != null);
  }


  @Nullable
  public Source retrieveSourceForCollection(String collectionId) {
    Source source = null;

    if (collectionId.equals("main")) {
      List<Source> sources = sourceRepository.findByType(SourceType.portal, null);
      if (!sources.isEmpty()) {
        source = sources.get(0);
      }
    } else {
      source = sourceRepository.findOneByUuid(collectionId);
    }

    return source;
  }


  public String retrieveCollectionFilter(Source source) {
    String collectionFilter = "";

    if (source.getType() == SourceType.subportal) {
        collectionFilter = source.getFilter();
    } else if (source.getType() == SourceType.harvester) {
      collectionFilter = String.format("+harvesterUuid:\"%s\"", source.getUuid());
    }

    return collectionFilter;
  }

}
