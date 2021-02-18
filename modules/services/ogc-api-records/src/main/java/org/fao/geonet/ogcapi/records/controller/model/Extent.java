package org.fao.geonet.ogcapi.records.rest.ogc.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.xml.bind.annotation.*;

/**
 * Extent
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-02-18T08:34:42.274802+01:00[Europe/Madrid]")
@JacksonXmlRootElement(localName = "Extent")
@XmlRootElement(name = "Extent")
@XmlAccessorType(XmlAccessType.FIELD)
public class Extent   {
  /**
   * Coordinate reference system of the coordinates in the spatial extent (property `spatial`). In the Core, only WGS84 longitude/latitude is supported. Extensions may support additional coordinate reference systems.
   */
  public enum CrsEnum {
    HTTP_WWW_OPENGIS_NET_DEF_CRS_OGC_1_3_CRS84("http://www.opengis.net/def/crs/OGC/1.3/CRS84");

    private String value;

    CrsEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static CrsEnum fromValue(String value) {
      for (CrsEnum b : CrsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @JsonProperty("crs")
  @JacksonXmlProperty(localName = "crs")
  private CrsEnum crs = CrsEnum.HTTP_WWW_OPENGIS_NET_DEF_CRS_OGC_1_3_CRS84;

  @JsonProperty("spatial")
  @JacksonXmlProperty(localName = "spatial")
  
  private List<BigDecimal> spatial = null;

  /**
   * Temporal reference system of the coordinates in the temporal extent (property `temporal`). In the Core, only the Gregorian calendar is supported. Extensions may support additional temporal reference systems.
   */
  public enum TrsEnum {
    HTTP_WWW_OPENGIS_NET_DEF_UOM_ISO_8601_0_GREGORIAN("http://www.opengis.net/def/uom/ISO-8601/0/Gregorian");

    private String value;

    TrsEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TrsEnum fromValue(String value) {
      for (TrsEnum b : TrsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @JsonProperty("trs")
  @JacksonXmlProperty(localName = "trs")
  private TrsEnum trs = TrsEnum.HTTP_WWW_OPENGIS_NET_DEF_UOM_ISO_8601_0_GREGORIAN;

  @JsonProperty("temporal")
  @JacksonXmlProperty(localName = "temporal")
  
  private List<String> temporal = null;

  public Extent crs(CrsEnum crs) {
    this.crs = crs;
    return this;
  }

  /**
   * Coordinate reference system of the coordinates in the spatial extent (property `spatial`). In the Core, only WGS84 longitude/latitude is supported. Extensions may support additional coordinate reference systems.
   * @return crs
  */
  @ApiModelProperty(value = "Coordinate reference system of the coordinates in the spatial extent (property `spatial`). In the Core, only WGS84 longitude/latitude is supported. Extensions may support additional coordinate reference systems.")
  public CrsEnum getCrs() {
    return crs;
  }

  public void setCrs(CrsEnum crs) {
    this.crs = crs;
  }

  public Extent spatial(List<BigDecimal> spatial) {
    this.spatial = spatial;
    return this;
  }

  public Extent addSpatialItem(BigDecimal spatialItem) {
    if (this.spatial == null) {
      this.spatial = new ArrayList<>();
    }
    this.spatial.add(spatialItem);
    return this;
  }

  /**
   * West, north, east, south edges of the spatial extent. The minimum and maximum values apply to the coordinate reference system WGS84 longitude/latitude that is supported in the Core. If, for example, a projected coordinate reference system is used, the minimum and maximum values need to be adjusted.
   * @return spatial
  */
  @ApiModelProperty(example = "[-180,-90,180,90]", value = "West, north, east, south edges of the spatial extent. The minimum and maximum values apply to the coordinate reference system WGS84 longitude/latitude that is supported in the Core. If, for example, a projected coordinate reference system is used, the minimum and maximum values need to be adjusted.")
  public List<BigDecimal> getSpatial() {
    return spatial;
  }

  public void setSpatial(List<BigDecimal> spatial) {
    this.spatial = spatial;
  }

  public Extent trs(TrsEnum trs) {
    this.trs = trs;
    return this;
  }

  /**
   * Temporal reference system of the coordinates in the temporal extent (property `temporal`). In the Core, only the Gregorian calendar is supported. Extensions may support additional temporal reference systems.
   * @return trs
  */
  @ApiModelProperty(value = "Temporal reference system of the coordinates in the temporal extent (property `temporal`). In the Core, only the Gregorian calendar is supported. Extensions may support additional temporal reference systems.")
  public TrsEnum getTrs() {
    return trs;
  }

  public void setTrs(TrsEnum trs) {
    this.trs = trs;
  }

  public Extent temporal(List<String> temporal) {
    this.temporal = temporal;
    return this;
  }

  public Extent addTemporalItem(String temporalItem) {
    if (this.temporal == null) {
      this.temporal = new ArrayList<>();
    }
    this.temporal.add(temporalItem);
    return this;
  }

  /**
   * Begin and end times of the temporal extent.
   * @return temporal
  */
  @ApiModelProperty(example = "[\"2011-11-11T12:22:11.000Z\",\"2012-11-24T12:32:43.000Z\"]", value = "Begin and end times of the temporal extent.")
  public List<String> getTemporal() {
    return temporal;
  }

  public void setTemporal(List<String> temporal) {
    this.temporal = temporal;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Extent extent = (Extent) o;
    return Objects.equals(this.crs, extent.crs) &&
        Objects.equals(this.spatial, extent.spatial) &&
        Objects.equals(this.trs, extent.trs) &&
        Objects.equals(this.temporal, extent.temporal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(crs, spatial, trs, temporal);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Extent {\n");
    
    sb.append("    crs: ").append(toIndentedString(crs)).append("\n");
    sb.append("    spatial: ").append(toIndentedString(spatial)).append("\n");
    sb.append("    trs: ").append(toIndentedString(trs)).append("\n");
    sb.append("    temporal: ").append(toIndentedString(temporal)).append("\n");
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

