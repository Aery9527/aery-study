package org.aery.study.feign.api;

import feign.*;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 在{@link FeignClient}裡的configuration指定的class不需要加上{@link Configuration}就會是spring裡的bean.
 * 除了透過以下bean方式設定Feign, 也可以透過設定值<b>feign.client.config.FEIGN_NAME.setting</b>設定,
 * <p>
 * feign.client.config.aery-feign.connectTimeout=5000
 * feign.client.config.aery-feign.readTimeout=5000
 * feign.client.config.aery-feign.loggerLevel=5000
 * feign.client.config.aery-feign.retryer=(CLASS QUALIFIED NAME)
 * feign.client.config.aery-feign.requestInterceptors[0]=(CLASS QUALIFIED NAME)
 * feign.client.config.aery-feign.requestInterceptors[1]=(CLASS QUALIFIED NAME)
 * feign.client.config.aery-feign.encoder=(CLASS QUALIFIED NAME)
 * feign.client.config.aery-feign.decoder=(CLASS QUALIFIED NAME)
 * feign.client.config.aery-feign.contract=(CLASS QUALIFIED NAME)
 * </p>
 */
public class FeignApiConfig {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    /**
     * 日誌配置.
     * 可以顯示Feign的IO狀態
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * 契約配置.
     * 原生的Feign不支持Spring MVC註解,
     */
//    @Bean
    public Contract feignContract() {
        return new feign.Contract.Default(); // Feign原生定義方式
//        return new SpringMvcContract(); // spring mvc定義方式
    }

    /**
     * Basic認證配置. 也可以換成任意自訂邏輯.
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new BasicAuthRequestInterceptor("user", "password");
    }

    @Bean
    public Request.Options options() {
        return new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true);
    }

    @Bean
    public Decoder decoder() {
        return new SpringDecoder(this.messageConverters);
    }

    @Bean
    public Encoder encoder() {
        return new SpringEncoder(this.messageConverters);
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default();
    }

}
