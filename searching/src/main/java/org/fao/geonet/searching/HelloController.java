package org.fao.geonet.searching;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class HelloController {

    @RequestMapping("/search")
    public String index() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map claims = (Map) SecurityContextHolder.getContext().getAuthentication().getDetails();
        List<Integer> viewingGroup = (List<Integer>) claims.get("_viewingGroup");
        return "Greetings from Spring Boot! " + name + ", " + viewingGroup.stream().map(x -> Integer.toString(x)).collect(Collectors.joining("|"));
    }

}
