/**
 * (c) 2020 Open Source Geospatial Foundation - all rights reserved This code is licensed under the
 * GPL 2.0 license, available at the root application directory.
 */

package org.fao.geonet.common.search.processor.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Sets;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.fao.geonet.common.search.Constants;
import org.fao.geonet.common.search.domain.Profile;
import org.fao.geonet.common.search.domain.ReservedGroup;
import org.fao.geonet.common.search.domain.ReservedOperation;
import org.fao.geonet.common.search.domain.UserInfo;
import org.fao.geonet.index.model.gn.IndexRecordFieldNames;
import org.springframework.stereotype.Component;


@Component("JsonUserAndSelectionAwareResponseProcessorImpl")
public class JsonUserAndSelectionAwareResponseProcessorImpl extends AbstractResponseProcessor {

  /**
   * Process the search response to add information about the user,
   * privileges and selection status.
   *
   */
  @Override
  public void processResponse(HttpSession httpSession,
      InputStream streamFromServer, OutputStream streamToClient,
      UserInfo userInfo, String bucket, Boolean addPermissions) throws Exception {
    JsonParser parser = parserForStream(streamFromServer);
    JsonGenerator generator = ResponseParser.jsonFactory.createGenerator(streamToClient);

    // TODO: Check to enable it
    //final Set<String> selections = (addPermissions ?
    //    SelectionManager.getManager(ApiUtils.getUserSession(httpSession)).getSelection(bucket)
    //    : new HashSet<>());
    Set<String> selections = new HashSet<>();

    new ResponseParser().matchHits(parser, generator, doc -> {
      if (addPermissions) {
        addUserInfo(doc, userInfo);
        addSelectionInfo(doc, selections);
      }

      // Remove fields with privileges info
      if (doc.has(IndexRecordFieldNames.source)) {
        ObjectNode sourceNode = (ObjectNode) doc.get(IndexRecordFieldNames.source);

        for (ReservedOperation o : ReservedOperation.values()) {
          sourceNode.remove(IndexRecordFieldNames.opPrefix + o.getId());
        }
      }
    }, true);
    generator.flush();
    generator.close();
  }


  /**
   * Add to a query result if it's selected.
   *
   * @param doc         Query result.
   * @param selections  List of selected metadata uuids.
   */
  protected void addSelectionInfo(ObjectNode doc, Set<String> selections) {
    final String uuid = getSourceFieldAsString(doc, IndexRecordFieldNames.uuid);
    doc.put(Constants.Elem.SELECTED, selections.contains(uuid));
  }

  /**
   * Add to a query result:
   *   - the owner information.
   *   - editing flag.
   *   - public flag.
   *   - operations allowed (view, edit, download, etc.) to the user.
   *
   * @param doc         Query result.
   * @param userInfo    User information.
   */
  protected void addUserInfo(ObjectNode doc, UserInfo userInfo) {
    final Integer owner = getSourceFieldAsInteger(doc, IndexRecordFieldNames.owner);
    final Integer groupOwner = getSourceFieldAsInteger(doc, IndexRecordFieldNames.groupOwner);

    ObjectMapper objectMapper = new ObjectMapper();

    //final AccessManager accessManager = context.getBean(AccessManager.class);
    //final boolean isOwner = accessManager.isOwner(context, sourceInfo);

    final boolean isOwner = isOwner(userInfo, owner, groupOwner);

    final HashSet<ReservedOperation> operations;
    boolean canEdit = false;
    if (isOwner) {
      operations = Sets.newHashSet(Arrays.asList(ReservedOperation.values()));
      if (owner != null) {
        doc.put("ownerId", owner.intValue());
      }
    } else {
      final Collection<Integer> groups = userInfo.getGroups();
      final Collection<Integer> editingGroups = userInfo.getEditingGroups();

      operations = Sets.newHashSet();
      for (ReservedOperation operation : ReservedOperation.values()) {
        final JsonNode operationNodes =
            doc.get(IndexRecordFieldNames.source)
                .get(IndexRecordFieldNames.opPrefix + operation.getId());
        if (operationNodes != null) {
          ArrayNode opFields = operationNodes.isArray()
              ? (ArrayNode) operationNodes : objectMapper.createArrayNode().add(operationNodes);
          if (opFields != null) {
            for (JsonNode field : opFields) {
              final int groupId = field.asInt();
              if (operation == ReservedOperation.editing
                  && canEdit == false
                  && editingGroups.contains(groupId)) {
                canEdit = true;
              }

              if (groups.contains(groupId)) {
                operations.add(operation);
              }
            }
          }
        }
      }
    }
    doc.put(Constants.Elem.EDIT, isOwner || canEdit);
    doc.put(IndexRecordFieldNames.owner, isOwner);
    doc.put(Constants.Elem.IS_PUBLISHED_TO_ALL,
        hasOperation(doc, ReservedGroup.all, ReservedOperation.view));
    addReservedOperation(doc, operations, ReservedOperation.view);
    addReservedOperation(doc, operations, ReservedOperation.notify);
    addReservedOperation(doc, operations, ReservedOperation.download);
    addReservedOperation(doc, operations, ReservedOperation.dynamic);
    addReservedOperation(doc, operations, ReservedOperation.featured);

    if (!operations.contains(ReservedOperation.download)) {
      doc.put(Constants.Elem.GUEST_DOWNLOAD,
          hasOperation(doc, ReservedGroup.guest, ReservedOperation.download));
    }
  }

  private Integer getFieldValueAsInteger(ObjectNode node, String name) {
    final JsonNode sub = node.get(name);
    return sub != null ? sub.asInt() : null;
  }

  private String getFieldValueAsString(ObjectNode node, String name) {
    final JsonNode sub = node.get(name);
    return sub != null ? sub.asText() : null;
  }

  /**
   * Retrieves a field from the source node as a string value.
   *
   * @param node  Node with metadata information.
   * @param name  Field name to retrieve from the source section.
   */
  private String getSourceFieldAsString(ObjectNode node, String name) {
    final JsonNode sourceNode = node.get(IndexRecordFieldNames.source);

    if (sourceNode != null) {
      final JsonNode sub = sourceNode.get(name);
      return sub != null ? sub.asText() : null;
    } else {
      return null;
    }
  }

  /**
   * Retrieves a field from the source node as a integer value.
   *
   * @param node  Node with metadata information.
   * @param name  Field name to retrieve from the source section.
   */
  private Integer getSourceFieldAsInteger(ObjectNode node, String name) {
    final JsonNode sourceNode = node.get(IndexRecordFieldNames.source);

    if (sourceNode != null) {
      final JsonNode sub = node.get(IndexRecordFieldNames.source).get(name);
      return sub != null ? sub.asInt() : null;
    } else {
      return null;
    }
  }

  private void addReservedOperation(ObjectNode doc, HashSet<ReservedOperation> operations,
      ReservedOperation kind) {
    doc.put(kind.name(), operations.contains(kind));
  }

  private boolean hasOperation(ObjectNode doc, ReservedGroup group, ReservedOperation operation) {
    ObjectMapper objectMapper = new ObjectMapper();
    int groupId = group.getId();
    final JsonNode operationNodes =
        doc.get(IndexRecordFieldNames.source)
            .get(IndexRecordFieldNames.opPrefix + operation.getId());
    if (operationNodes != null) {
      ArrayNode opFields = operationNodes.isArray()
          ? (ArrayNode) operationNodes : objectMapper.createArrayNode().add(operationNodes);
      if (opFields != null) {
        for (JsonNode field : opFields) {
          if (groupId == field.asInt()) {
            return true;
          }
        }
      }
    }
    return false;
  }


  private boolean isOwner(UserInfo userInfo, Integer owner, Integer groupOwner) {

    if (!userInfo.isAuthenticated()) {
      return false;
    }

    //--- check if the user is an administrator
    final Profile profile = Profile.findProfileIgnoreCase(userInfo.getHighestProfile());
    if (profile == Profile.Administrator) {
      return true;
    }

    //--- check if the user is the metadata owner
    //
    if (owner != null
        && userInfo.getUserId() == owner) {
      return true;
    }

    //--- check if the user is a reviewer or useradmin
    if (profile != Profile.Reviewer && profile != Profile.UserAdmin) {
      return false;
    }

    //--- if there is no group owner then the reviewer cannot review
    //--- and the useradmin cannot administer
    if (groupOwner == null) {
      return false;
    }
    for (Integer userGroup : getReviewerGroups(userInfo)) {
      if (userGroup == groupOwner.intValue()) {
        return true;
      }
    }
    return false;
  }

  private Set<Integer> getReviewerGroups(UserInfo userInfo) {
    Set<Integer> hs = new HashSet<Integer>();

    // get other groups
    if (userInfo.isAuthenticated()) {
      // TODO: Retrieve the user groups where the user is a Reviewer
    }
    return hs;
  }
}
