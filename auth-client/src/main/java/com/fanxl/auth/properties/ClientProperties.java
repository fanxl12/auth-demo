package com.fanxl.auth.properties;

import lombok.Data;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/15 0015 21:20
 */
@Data
public class ClientProperties {

    /**
     * client_id
     */
    private String id;

    /**
     * client_secret
     */
    private String secret;

    /**
     * sso单点登录code码的回调服务器地址
     */
    private String callback;

    /**
     * sso单点登录成功之后回调地址
     */
    private String state;

}
