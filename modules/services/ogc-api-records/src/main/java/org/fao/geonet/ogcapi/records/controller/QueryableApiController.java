package org.fao.geonet.ogcapi.records.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.fao.geonet.ogcapi.records.model.JsonSchema;
import org.fao.geonet.ogcapi.records.service.QueryablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import springfox.documentation.annotations.ApiIgnore;

/**
 * See https://docs.ogc.org/is/19-079r2/19-079r2.html#rc_queryables and
 * https://docs.ogc.org/DRAFTS/20-004.html#_queryables_link
 */
@Api(tags = "OGC API Records")
@Controller
@Slf4j(topic = "org.fao.geonet.ogcapi")
public class QueryableApiController {

  @Autowired
  QueryablesService queryablesService;

  /**
   * Describe queryables for a collection.
   */
  @io.swagger.v3.oas.annotations.Operation(
      summary = "Describes queryables for a collection.",
      description = "Queryables resource for discovering a list of resource properties with their "
          + "types and constraints that may be used to construct filter expressions"
          + " on a collection of resources.")
  @GetMapping(value = "/collections/{collectionId}/queryables",
      produces = {MediaType.APPLICATION_JSON_VALUE,
      })
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Describe queryables for a collection.")
  })
  @ResponseBody
  public ResponseEntity<JsonSchema> queryablesForCollection(
      @ApiParam(value = "Identifier (name) of a specific collection", required = true)
      @PathVariable("collectionId") String collectionId,
      @ApiIgnore HttpServletRequest request,
      @ApiIgnore HttpServletResponse response,
      @ApiIgnore Model model) throws Exception {

    var jsonSchema = queryablesService.buildQueryables(collectionId);

    return ResponseEntity.ok(jsonSchema);
  }
}
