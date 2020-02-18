package com.fanxl.auth.constant;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/17 0017 20:50
 */
public class AuthConstant {

    /**
     * request中保存的token的key
     */
    public static final String TOKEN_KEY = "token_key";

    /**
     * request中保存的刷新token的key
     */
    public static final String REFRESH_TOKEN_KEY = "refresh_token_key";

    /**
     * cookie认证模式下，返回给浏览器的token名称
     */
    public static final String COOKIE_ACCESS_TOKEN_NAME = "fan_access_token";

    /**
     * cookie认证模式下，返回给浏览器的刷新token名称
     */
    public static final String COOKIE_REFRESH_TOKEN_NAME = "fan_refresh_token";

    /**
     * cookie认证模式下，返回给浏览器刷新token有效期
     */
    public static final Integer COOKIE_REFRESH_TOKEN_TIME = 2592000;

    /**
     * session认证模式下，存在session中的认证信息key
     */
    public static final String SESSION_TOKEN_KEY = "token";

    public static final String TOKEN_PREFIX = "";

    /**
     * token服务器后缀
     */
    public static final String TOKEN_SERVER = "/oauth/token";

    /**
     * 认证服务器后缀
     */
    public static final String AUTH_SERVER = "/oauth/authorize";

    /**
     * 认证回调地址后缀
     */
    public static final String CALL_BACK = "/oauth/callback";

}
