package org.fao.geonet.ogcapi.records.rest.ogc.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.xml.bind.annotation.*;

/**
 * ReqClasses
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-02-18T08:34:42.274802+01:00[Europe/Madrid]")
@JacksonXmlRootElement(localName = "ReqClasses")
@XmlRootElement(name = "ReqClasses")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReqClasses   {
  @JsonProperty("conformsTo")
  @JacksonXmlProperty(localName = "conformsTo")
  
  private List<String> conformsTo = new ArrayList<>();

  public ReqClasses conformsTo(List<String> conformsTo) {
    this.conformsTo = conformsTo;
    return this;
  }

  public ReqClasses addConformsToItem(String conformsToItem) {
    this.conformsTo.add(conformsToItem);
    return this;
  }

  /**
   * Get conformsTo
   * @return conformsTo
  */
  @ApiModelProperty(example = "[\"http://www.opengis.net/spec/ogcapi-common/1.0/conf/core\",\"http://www.opengis.net/spec/ogcapi-common/1.0/conf/collections\",\"http://www.opengis.net/spec/ogcapi-common/1.0/conf/oas3\",\"http://www.opengis.net/spec/ogcapi-common/1.0/conf/html\",\"http://www.opengis.net/spec/ogcapi-common/1.0/conf/geojson\",\"http://www.opengis.net/spec/ogcapi-coverages/1.0/conf/core\"]", required = true, value = "")
  public List<String> getConformsTo() {
    return conformsTo;
  }

  public void setConformsTo(List<String> conformsTo) {
    this.conformsTo = conformsTo;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReqClasses reqClasses = (ReqClasses) o;
    return Objects.equals(this.conformsTo, reqClasses.conformsTo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(conformsTo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReqClasses {\n");
    
    sb.append("    conformsTo: ").append(toIndentedString(conformsTo)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

