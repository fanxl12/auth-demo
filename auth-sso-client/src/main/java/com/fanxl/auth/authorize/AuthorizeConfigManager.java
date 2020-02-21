package com.fanxl.auth.authorize;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * @description 授权信息管理器
 * 用于收集系统中所有 AuthorizeConfigProvider 并加载其配置
 * @author: fanxl
 * @date: 2020/2/21 0021 20:59
 */
public interface AuthorizeConfigManager {

    void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);

}
