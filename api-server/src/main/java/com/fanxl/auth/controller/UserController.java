package com.fanxl.auth.controller;

import com.fanxl.auth.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/14 0014 17:43
 */
@Slf4j
@RestController
public class UserController {

    @Autowired
    private SecurityProperties securityProperties;

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return securityProperties.getAuthServer() + "/logout?redirect_uri=" + securityProperties.getLogoutRedirectUrl();
    }
}
