package org.geonetworkopensource.gnconfigserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class GnConfigApp {
	public static void main(String[] args) {
		SpringApplication.run(GnConfigApp.class, args);
	}
}
