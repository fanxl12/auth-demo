package com.fanxl.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/14 0014 17:43
 */
@Slf4j
@RequestMapping("/api")
@RestController
public class ApiController {

    @GetMapping("")
    public String get() {
        return "you get api information";
    }

    @GetMapping("/test")
    public String test(HttpServletRequest request, HttpServletResponse response) {
//        log.info("requestSessionId:{}, sessionId:{}", request.getRequestedSessionId(), request.getSession().getId());
//        if (request.getSession().getAttribute("test") != null) {
//            log.info("设置session");
//            request.getSession().setAttribute("test", "test");
//        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("name:{}, value:{}", cookie.getName(), cookie.getValue());
            }
        } else {
            Cookie accessTokenCookie = new Cookie("test_token", "123456789");
            accessTokenCookie.setMaxAge(60);
            accessTokenCookie.setDomain("vue.fxl.com");
            accessTokenCookie.setPath("/");
            response.addCookie(accessTokenCookie);
        }
        return "you get api test";
    }
}
