package com.baker.learning.bigdatacommons;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * restTemplate配置
 */
@Configuration
public class RestTemplateConfig {
    /**
     * 设置
     *
     * @param factory
     * @return
     */
    @Bean
    public RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public HttpComponentsClientHttpRequestFactory simpleClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        //达到最大请求时间，请求将自动视为失败，结束当前进程。
        factory.setReadTimeout(20000);  //单位为ms
        factory.setConnectTimeout(20000); //单位为ms
        return factory;
    }

}
