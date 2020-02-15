package com.fanxl.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/14 0014 17:43
 */
@RequestMapping("/user")
@Controller
public class UserController {

    @GetMapping("")
    public String all(Model model) {
        model.addAttribute("name", "李四");
        return "/user/me";
    }

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("name", "张三");
        return "/user/list";
    }

    @GetMapping("/login")
    public void login(HttpServletResponse response) throws Exception {
        String url = "http://auth.fan.com:8011/oauth/authorize?" +
                "client_id=flow-app&" +
                "redirect_uri=http://web.fan.com:8016/oauth/callback&" +
                "response_type=code&" +
                "state=abc";
        response.sendRedirect(url);
    }

}
