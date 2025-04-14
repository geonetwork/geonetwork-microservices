package org.fao.geonet.index.model.geojson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
  private String name;
  private String positionName;
  private String organization;
  private Link logo;
  private String phone;
  private String email;
  private Address address;
  private List<Link> links;
  // TODO: hoursOfService
  private String contactInstructions;
  private List<Role> roles;



}
