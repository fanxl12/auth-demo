package com.fanxl.auth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/14 0014 17:43
 */
@RequestMapping("/user")
@Controller
public class UserController {

    @GetMapping("")
    public String all(Model model, @AuthenticationPrincipal String username) {
        model.addAttribute("name", username);
        return "/user/me";
    }

    @GetMapping("list")
    public String list(Model model) {
        model.addAttribute("name", "张三");
        return "/user/list";
    }

}
