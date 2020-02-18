package com.fanxl.auth.config;

import com.fanxl.auth.service.AuthSetService;
import com.fanxl.auth.service.AuthSetServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
}
