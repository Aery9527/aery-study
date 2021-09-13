package org.aery.study.feign.client;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("org.aery.study.feign.api")
public class FeignClientConfig {
}
