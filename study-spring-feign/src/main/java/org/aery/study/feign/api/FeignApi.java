package org.aery.study.feign.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "aery-feign", url = "http://localhost:8080", configuration = FeignApiConfig.class)
public interface FeignApi {

    @GetMapping(value = "/test1")
    String test1(@RequestParam("a") String kerker);

    @GetMapping(value = "/test2")
    FeignEnum test2(@RequestParam("b") FeignEnum feignEnum);

    @GetMapping(value = "/test3")
    Map<String, Object> test3(@RequestParam Map<String, Object> param);

    @PostMapping("/test")
    FeignApiVo1 test4(@RequestBody FeignApiVo1 vo1);

}
