package com.fanxl.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableOAuth2Sso
public class ClientWebsecurityConfigurer extends WebSecurityConfigurerAdapter {

    /**
     * 用于访问认证服务器
     */
    @Autowired
    private ResourceServerTokenServices tokenServices;


    @Override
    public void configure(HttpSecurity http) throws Exception {

        /**
         * 创建自定义Filter，有两种构造方式，分别对应不同的架构模式
         */
        AppAuthenticationProcessFilter appAuthenticationProcessFilter = new AppAuthenticationProcessFilter(tokenServices);

        http.addFilterAfter(appAuthenticationProcessFilter, UsernamePasswordAuthenticationFilter.class)
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**","/callback/", "/webjars/**", "/error**", "/oauth/**")
                    .permitAll()
                .anyRequest()
                .authenticated();
    }
}