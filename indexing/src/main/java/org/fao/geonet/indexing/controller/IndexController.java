package org.fao.geonet.indexing.controller;

import io.micrometer.core.annotation.Timed;
import lombok.val;
import org.fao.geonet.indexing.service.IndexingManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping(value = "index")
public class IndexController {
    @Autowired
    IndexingManagerImpl indexingManager;

    @Autowired
    MessageSource messages;

    @GetMapping(path = "/all")
    public ResponseEntity indexAll(
        @RequestHeader(value = "Accept-Language", required = false)
            String locale
    ) {
        val message = messages.getMessage("index.status.available",null, Locale.forLanguageTag(locale));

        indexingManager.indexAll();

//        if (indexFound) {
            return ResponseEntity.ok().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
    }
}
