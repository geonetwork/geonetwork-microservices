package org.fao.geonet.authorizing;

import org.fao.geonet.common.JwtTokenUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@Import({SecurityConfigurer.class,  JwtAuthenticationController.class, JwtTokenUtil.class})
public class WebApp {

    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
    }
}
