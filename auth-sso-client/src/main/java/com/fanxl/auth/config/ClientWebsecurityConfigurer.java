package com.fanxl.auth.config;

import com.fanxl.auth.authorize.AuthorizeConfigManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @description 授权服务配置
 * @author: fanxl
 * @date: 2020/2/21 0021 21:00
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableOAuth2Sso
public class ClientWebsecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthorizeConfigManager authorizeConfigManager;

    @Autowired
    private AppAuthenticationConfig appAuthenticationConfig;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.GET, "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.gif", "/**/*.jpg");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 配置token认证的过滤器配置
        appAuthenticationConfig.configure(http);
        // 配置其他配置
        authorizeConfigManager.config(http.authorizeRequests());
    }
}