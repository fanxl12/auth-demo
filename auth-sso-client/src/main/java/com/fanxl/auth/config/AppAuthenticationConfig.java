package com.fanxl.auth.config;

import com.fanxl.auth.filter.AppAuthenticationProcessFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @description {@link AppAuthenticationProcessFilter} 的配置类
 * @author: fanxl
 * @date: 2020/2/21 0021 21:16
 */
@Component
public class AppAuthenticationConfig {

    /**
     * 用于访问认证服务器
     */
    @Autowired
    private ResourceServerTokenServices tokenServices;

    public void configure(HttpSecurity http) throws Exception {
        /**
         * 创建自定义Filter，有两种构造方式，分别对应不同的架构模式
         */
        AppAuthenticationProcessFilter appAuthenticationProcessFilter = new AppAuthenticationProcessFilter(tokenServices);
        http.addFilterAfter(appAuthenticationProcessFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
