package org.aery.study.feign.client;

import org.aery.study.feign.api.FeignApi;
import org.aery.study.feign.api.FeignApiVo1;
import org.aery.study.feign.api.FeignEnum;
import org.aery.study.feign.server.FeignServerApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = {
                FeignServerApplication.class
                , FeignClientConfig.class
        })
public class FeignClientTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @Autowired
    private FeignApi feignApi;

    @Test
    public void test() {
        Map<String, FeignApi> feignApis = this.applicationContext.getBeansOfType(FeignApi.class);
        feignApis.forEach((beanName, bean) -> {
            this.logger.info("FeignApi bean : " + beanName + " (" + bean + "(" + bean.getClass() + "))");
        });
        this.logger.info("Autowired feignApi : " + this.feignApi.toString() + "(" + this.feignApi.getClass() + ")");
    }

    @Test
    public void test1() {
        String result = invoke(() -> this.feignApi.test1("9527"));
        this.logger.info(result);
    }

    @Test
    public void test2() {
        FeignEnum result = invoke(() -> this.feignApi.test2(FeignEnum.Rion));
        this.logger.info(result.toString());
    }

    @Test
    public void test3() {
        Map<String, Object> param = new HashMap<>();
        param.put("a", "kerker");
        param.put("b", 9527);

        Map<String, Object> result = invoke(() -> this.feignApi.test3(param));
        this.logger.info(result.toString());
    }

    @Test
    public void test4() {
        FeignApiVo1 vo1 = new FeignApiVo1();
        vo1.setA("9527");
        vo1.setB(5566);

        FeignApiVo1 result = invoke(() -> this.feignApi.test4(vo1));
        this.logger.info(result.toString());
    }

    private <T> T invoke(Supplier<T> action) {
        StackTraceElement[] stes = Thread.currentThread().getStackTrace();
        StackTraceElement ste = stes[2];
        String methodName = ste.getMethodName();
        try {
            this.logger.info("Feign Method Start [" + methodName + "]");
            return action.get();
        } finally {
            this.logger.info("Feign Method finish [" + methodName + "]");
        }
    }

}
