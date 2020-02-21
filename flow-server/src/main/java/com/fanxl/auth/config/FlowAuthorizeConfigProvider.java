package com.fanxl.auth.config;

import com.fanxl.auth.authorize.AuthorizeConfigProvider;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * @description 自定义的授权配置，可以把项目里面不需要授权的排除掉
 * @author: fanxl
 * @date: 2020/2/21 0021 21:04
 */
@Component
@Order(Integer.MIN_VALUE)
public class FlowAuthorizeConfigProvider implements AuthorizeConfigProvider {

    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers("/test/not").permitAll();
        return false;
    }
}
