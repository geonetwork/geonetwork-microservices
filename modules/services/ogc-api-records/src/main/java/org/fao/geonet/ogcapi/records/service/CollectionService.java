package org.fao.geonet.ogcapi.records.service;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.common.search.SearchConfiguration;
import org.fao.geonet.domain.Source;
import org.fao.geonet.domain.SourceType;
import org.fao.geonet.domain.UiSetting;
import org.fao.geonet.repository.SourceRepository;
import org.fao.geonet.repository.UiSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j(topic = "org.fao.geonet.ogcapi.records")
public class CollectionService {

  @Autowired
  private SearchConfiguration configuration;

  @Autowired
  private SourceRepository sourceRepository;

  @Autowired
  private UiSettingsRepository uiSettingsRepository;

  /**
   * Checks if a collection is defined.
   */
  public boolean existsCollection(String collectionId) {
    Source source = retrieveSourceForCollection(collectionId);

    return (source != null);
  }


  /**
   * Retrieves the Source object related to a collection.
   */
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

  /**
   * Retrieves the ElasticSearch filter related to a collection.
   */
  public String retrieveCollectionFilter(Source source, boolean escape) {
    String collectionFilter = "";

    if (source.getType() == SourceType.subportal) {
      collectionFilter = source.getFilter();
    } else if (source.getType() == SourceType.harvester) {
      collectionFilter = String.format("+harvesterUuid:\"%s\"", source.getUuid());
    }

    return escape
        ? collectionFilter.replace("\"", "\\\"")
        : collectionFilter;
  }

  /**
   * Retrieves the sortables related to a collection from the user interface configuration.
   */
  public List<String> getSortables(Source source) {
    List<String> sortables = new ArrayList<>();

    Optional<UiSetting> uiSetting = Optional.empty();
    if (source.getType() == SourceType.portal) {
      uiSetting = uiSettingsRepository.findById("srv");
    } else if (source.getUiConfig() != null) {
      uiSetting = uiSettingsRepository.findById(source.getUiConfig());
    }

    if (uiSetting.isPresent()) {
      UiSetting uiSettingValue = uiSetting.get();
      String uiSettingValueConfiguration = uiSettingValue.getConfiguration();

      try {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = mapper.getFactory();
        JsonParser parser = factory.createParser(uiSettingValueConfiguration);
        JsonNode actualObj = mapper.readTree(parser);

        JsonNode sortbyValues = actualObj.get("mods").get("search").get("sortbyValues");

        if (sortbyValues != null) {
          sortbyValues.iterator().forEachRemaining(s -> sortables.add(s.get("sortBy").textValue()));
        }
      } catch (Exception ex) {
        log.error(ex.getMessage(), ex);
      }
    }

    if (sortables.isEmpty()) {
      sortables.addAll(configuration.getSortables());
    }

    return sortables;
  }

}
