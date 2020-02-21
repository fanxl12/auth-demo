package com.fanxl.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/14 0014 17:43
 */
@Slf4j
@RequestMapping("/test")
@RestController
public class TestController {

    @GetMapping("")
    public String get(HttpServletRequest request) {
        log.info("requestSessionId:{}, sessionId:{}", request.getRequestedSessionId(), request.getSession().getId());
        return "you get api information";
    }

    @RequestMapping("/me")
    public Principal user(Principal principal) {
        log.info("用户信息:{}", principal);
        return principal;
    }
}
