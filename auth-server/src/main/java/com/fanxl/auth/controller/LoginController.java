package com.fanxl.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/20 0020 12:07
 */
@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

}
