package com.fanxl.auth.authorize;

import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * @description 核心模块的授权配置提供器，安全模块涉及的url的授权配置在这里。
 * @author: fanxl
 * @date: 2020/2/21 0021 21:01
 */
@Component
@Order(Integer.MIN_VALUE)
public class FanAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        // 可以在此忽略一些默认的url
        config.mvcMatchers("/login**", "/callback/", "/webjars/**", "/error**", "/oauth/**").permitAll();
        return false;
    }
}
