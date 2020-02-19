package com.fanxl.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * @description
 * @author: fanxl
 * @date: 2020/2/15 0015 21:18
 */
@Configuration
@ConfigurationProperties(prefix = "auth.security")
@EnableConfigurationProperties(SecurityProperties.class)
@Data
public class SecurityProperties {

    private ClientProperties client = new ClientProperties();

    /**
     * sso单点登录服务器地址
     */
    private String authServer;

    /**
     * token认证服务器地址
     */
    private String tokenServer;

    /**
     * 认证管理类型 {@link com.fanxl.auth.constant.AuthType}
     */
    private String type;

    /**
     * 重定向类型 {@link com.fanxl.auth.constant.RedirectType}
     */
    private String redirectType;

    /**
     * 退出登录地址
     */
    private String logout;

    /**
     * 退出登录成功之后回调地址
     */
    private String logoutRedirectUrl;
}
