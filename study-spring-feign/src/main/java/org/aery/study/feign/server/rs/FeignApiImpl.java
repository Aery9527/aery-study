package org.aery.study.feign.server.rs;

import org.aery.study.feign.api.FeignApi;
import org.aery.study.feign.api.FeignApiVo1;
import org.aery.study.feign.api.FeignEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class FeignApiImpl implements FeignApi {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String test1(String kerker) {
        this.logger.info("receive : " + kerker);
        return kerker;
    }

    @Override
    public FeignEnum test2(FeignEnum feignEnum) {
        this.logger.info("receive : " + feignEnum);
        return feignEnum;
    }

    @Override
    public Map<String, Object> test3(Map<String, Object> param) {
        this.logger.info("receive : " + param);
        return param;
    }

    @Override
    public FeignApiVo1 test4(FeignApiVo1 vo1) {
        this.logger.info("receive : " + vo1);
        return vo1;
    }

    @Override
    public String test5(String kerker) {
        this.logger.info("receive : " + kerker);
        return kerker;
    }

}
