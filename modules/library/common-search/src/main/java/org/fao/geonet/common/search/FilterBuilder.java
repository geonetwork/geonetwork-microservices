/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.fao.geonet.common.search;

import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.fao.geonet.common.search.domain.Profile;
import org.fao.geonet.common.search.domain.ReservedOperation;
import org.fao.geonet.common.search.domain.UserInfo;
import org.springframework.stereotype.Component;


@Component
public class FilterBuilder {
  private static final String filterTemplate = " {\n"
      + "       \t\"query_string\": {\n"
      + "       \t\t\"query\": \"%s\"\n"
      + "       \t}\n"
      + "}";


  /**
   * Builds a query filter including a privilege filter, document type and portal filters.
   *
   * @param type      Document type.
   * @param userInfo  User details.
   * @return Query filter.
   */
  public String buildQueryFilter(String type, UserInfo userInfo)  {
    StringBuilder query = new StringBuilder();
    query.append(buildPermissionsFilter(userInfo).trim());

    if (type.equalsIgnoreCase("metadata")) {
      query.append(" AND (isTemplate:n)");
    } else if (type.equalsIgnoreCase("template")) {
      query.append(" AND (isTemplate:y)");
    } else if (type.equalsIgnoreCase("subtemplate")) {
      query.append(" AND (isTemplate:s)");
    }
    query.append(" AND (draft:n OR draft:e)");

    final String portalFilter = buildPortalFilter();
    if (!StringUtils.isEmpty(portalFilter)) {
      query.append(" ").append(portalFilter);
    }
    return String.format(filterTemplate, query);
  }

  /**
   * Adds a privileges filter to query the metadata allowed to the user.
   *
   * @param userInfo  User information.
   * @return Permissions filter.
   */
  private String buildPermissionsFilter(UserInfo userInfo) {
    // If admin you can see all
    if (Profile.Administrator.name().equals(userInfo.getHighestProfile())) {
      return "*";
    } else {
      // op0 (ie. view operation) contains one of the ids of your groups
      Set<Integer> groups = userInfo.getGroups();
      final String ids = groups.stream().map(Object::toString).map(e -> e.replace("-", "\\\\-"))
          .collect(Collectors.joining(" OR "));
      String operationFilter = String.format("op%d:(%s)", ReservedOperation.view.getId(), ids);

      if (userInfo.isAuthenticated() && userInfo.getUserId() != null) {
        // OR you are owner
        String ownerFilter = String.format("owner:%d", userInfo.getUserId());
        // OR member of groupOwner
        // TODOES
        return String.format("(%s %s)", operationFilter, ownerFilter);
      } else {
        return operationFilter;
      }
    }
  }

  /**
   * Document type (metadata, template, subtemplate) filter.
   *
   * @return  Document type filter.
   */
  private String buildDocTypeFilter(String type) {
    return "documentType:" + type;
  }


  /**
   * Portal filter.
   *
   * @return Portal filter.
   */
  private String buildPortalFilter() {
    // TODO: Check to use the portal information and check that is a valid value

    // If the requested portal define a filter
    // Add it to the request.
    /*if (node != null && !Constants.NodeInfo.DEFAULT_NODE.equals(node.getId())) {
      final Optional<Source> portal = sourceRepository.findById(node.getId());
      if (!portal.isPresent()) {
        LOGGER.warn("Null portal " + node);
      } else if (StringUtils.isNotEmpty(portal.get().getFilter())) {
        LOGGER.debug("Applying portal filter: {}", portal.get().getFilter());
        return portal.get().getFilter();
      }
    }*/
    return "";
  }
}
