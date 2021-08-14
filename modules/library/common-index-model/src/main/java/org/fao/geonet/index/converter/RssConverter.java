/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.index.converter;

import static org.fao.geonet.index.model.gn.IndexRecordFieldNames.CommonField.defaultText;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.fao.geonet.index.model.gn.IndexRecord;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames;
import org.fao.geonet.index.model.gn.Overview;
import org.fao.geonet.index.model.gn.ResourceDate;
import org.fao.geonet.index.model.rss.Enclosure;
import org.fao.geonet.index.model.rss.Guid;
import org.fao.geonet.index.model.rss.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "org.fao.geonet.index.converter")
public class RssConverter {

  public static DateTimeFormatter rssDateFormat = DateTimeFormatter.RFC_1123_DATE_TIME;

  @Autowired
  FormatterConfiguration formatterConfiguration;

  /**
   * Convert JSON index document _source node to RSS Item.
   *
   * <p>GeoNetwork 3 implementation: See https://github.com/geonetwork/core-geonetwork/blob/master/web/src/main/webapp/xslt/services/rss/rss-utils.xsl
   *
   * <p>Differences:
   * * No GeoRSS support * Link only target the landing page of the record
   *
   * <p>Validation: https://validator.w3.org/feed/check.cgi
   */
  public Item convert(ObjectNode doc) {
    try {
      IndexRecord record = new ObjectMapper()
          .readValue(doc.get(IndexRecordFieldNames.source).toString(), IndexRecord.class);

      // https://www.rssboard.org/rss-specification#hrelementsOfLtitemgt
      Item item = new Item();
      Guid guid = new Guid();
      guid.setIsPermaLink(false);
      guid.setValue(record.getMetadataIdentifier());
      item.setGuid(guid);
      item.setTitle(record.getResourceTitle().get(defaultText));
      item.setDescription(buildDescription(record));
      item.setLink(formatterConfiguration.buildLandingPageLink(record.getMetadataIdentifier()));

      Optional<Overview> overview = record.getOverview().stream().findFirst();
      if (overview.isPresent()) {
        Enclosure enclosure = new Enclosure();
        String url = overview.get().getUrl();
        enclosure.setUrl(url);
        String extension = url.substring(url.lastIndexOf(".") + 1);
        enclosure.setType("image/" + extension);
        item.setEnclosure(enclosure);
      }

      // Email address of the author of the item.
      record.getContact().forEach(c ->
          item.setAuthor(c.getEmail())
      );

      // Includes the item in one or more categories.
      // Category could be tag ? Was hardcoded in GN3

      // Indicates when the item was published.
      // Publication date first, any resource date and fallback to record date
      List<ResourceDate> resourceDates = record.getResourceDate();
      Optional<ResourceDate> publicationDate = resourceDates.stream()
          .filter(d -> "publication".equals(d.getType())).findFirst();
      Optional<ResourceDate> firstDate = resourceDates.stream().findFirst();
      String pubDate;
      if (publicationDate.isPresent()) {
        pubDate = publicationDate.get().getDate();
      } else if (firstDate.isPresent()) {
        pubDate = firstDate.get().getDate();
      } else {
        pubDate = record.getDateStamp();
      }
      if (StringUtils.isNotEmpty(pubDate)) {
        try {
          if (pubDate.length() == 10) {
            pubDate += "T12:00:00";
          }
          item.setPubDate(OffsetDateTime.parse(pubDate).format(rssDateFormat));
        } catch (DateTimeParseException parseException) {
          log.warn(String.format(
              "Failed to parse date %s in record %s",
              pubDate, record.getMetadataIdentifier()));
        }
      }

      return item;
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

  private String buildDescription(IndexRecord record) {
    return record.getResourceAbstract().get(defaultText);
  }

}
