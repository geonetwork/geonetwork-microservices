package org.fao.geonet.searching;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  /**
   * Search.
   */
  @RequestMapping("/search")
  public String search() {
    String name = SecurityContextHolder.getContext().getAuthentication().getName();
    Map claims = (Map) SecurityContextHolder.getContext().getAuthentication().getDetails();
    List<Integer> viewingGroup = (List<Integer>) claims.get("_viewingGroup");
    return "Search service called. You are authenticated as " + name + ", "
        + viewingGroup.stream().map(x -> Integer.toString(x)).collect(Collectors.joining("|"));
  }

}
