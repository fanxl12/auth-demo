package com.fanxl.auth.controller;

import com.fanxl.auth.config.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/14 0014 20:18
 */
@RequestMapping("/test")
@RestController
public class TestController {

    @GetMapping("")
    public User user(@AuthenticationPrincipal User user) {
        return user;
    }

    @GetMapping("/id")
    public Long getId(@AuthenticationPrincipal(expression = "#this.id") Long userId) {
        return userId;
    }


}
