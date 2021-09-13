package org.aery.study.feign.client;

import org.aery.study.feign.api.FeignApi;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FeignClientApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(FeignClientApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);

        FeignApi feignApi = applicationContext.getBean(FeignApi.class);
        String result = feignApi.test1("9527");
        System.out.println(result);
    }

}
