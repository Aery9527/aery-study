package org.aery.study.feign.server;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class FeignServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(FeignServerApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}
