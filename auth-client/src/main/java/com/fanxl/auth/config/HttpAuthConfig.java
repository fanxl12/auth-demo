package com.fanxl.auth.config;

import com.fanxl.auth.service.AuthSetService;
import com.fanxl.auth.service.AuthSetServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/18 0018 20:09
 */
@Configuration
public class HttpAuthConfig {

    @Bean
    @ConditionalOnMissingBean(AuthSetService.class)
    public AuthSetService authSetService() {
        return new AuthSetServiceImpl();
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        return factory;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setErrorHandler(new MyErrorHandler());
        return restTemplate;
    }

}
