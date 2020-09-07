package org.fao.geonet.searching;

import org.fao.geonet.common.JwtTokenUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Import;


@SpringBootApplication
@RefreshScope
@Import({JwtTokenUtil.class, HelloController.class})
public class GnSearchApp {

    public static void main(String[] args) {
        SpringApplication.run(GnSearchApp.class, args);
    }
}
