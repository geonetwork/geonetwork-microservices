package org.fao.geonet.ogcapi.records.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import org.fao.geonet.domain.Source;
import org.fao.geonet.index.model.gn.IndexRecord;

/**
 * CollectionInfoExtended entity.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CollectionInfoExtended  extends CollectionInfo {

  @JsonProperty("source")
  @JacksonXmlProperty(localName = "source")
  private Source source;

  @JsonProperty("record")
  @JacksonXmlProperty(localName = "record")
  private IndexRecord record;

  /**
   * Get source.
   */
  @ApiModelProperty(required = true, value = "")
  public Source getSource() {
    return source;
  }

  public void setSource(Source source) {
    this.source = source;
  }

  /**
   * Get record.
   */
  @ApiModelProperty(value = "")
  public IndexRecord getRecord() {
    return record;
  }

  public void setRecord(IndexRecord record) {
    this.record = record;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    CollectionInfoExtended that = (CollectionInfoExtended) o;
    return Objects.equals(source, that.source) && Objects.equals(record, that.record);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), source, record);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CollectionInfoExtended {\n");

    sb.append("    name: ").append(toIndentedString(getId())).append("\n");
    sb.append("    title: ").append(toIndentedString(getTitle())).append("\n");
    sb.append("    description: ").append(toIndentedString(getDescription())).append("\n");
    sb.append("    links: ").append(toIndentedString(getLinks())).append("\n");
    sb.append("    extent: ").append(toIndentedString(getExtent())).append("\n");
    sb.append("    crs: ").append(toIndentedString(getCrs())).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    record: ").append(toIndentedString(record)).append("\n");
    sb.append("}");
    return sb.toString();
  }


  /**
   * Builds a CollectionInfoExtended from a CollectionInfo instance.
   *
   */
  public static CollectionInfoExtended from(CollectionInfo collectionInfo) {
    CollectionInfoExtended collectionInfoExtended = new CollectionInfoExtended();
    collectionInfoExtended.setId(collectionInfo.getId());
    collectionInfoExtended.setTitle(collectionInfo.getTitle());
    collectionInfoExtended.setDescription(collectionInfo.getDescription());
    collectionInfoExtended.setCrs(collectionInfo.getCrs());
    collectionInfoExtended.setExtent(collectionInfo.getExtent());
    collectionInfoExtended.setLinks(collectionInfo.getLinks());

    return collectionInfoExtended;
  }
}

