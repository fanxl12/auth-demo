package com.fanxl.auth.utils;

import javax.servlet.http.Cookie;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/17 0017 21:19
 */
public class CookieUtil {

    public static Cookie getCookie(String name, String value, int maxAge, String domain) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(domain);
        cookie.setPath("/");
        return cookie;
    }


}
