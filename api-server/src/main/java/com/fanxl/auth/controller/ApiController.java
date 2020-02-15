package com.fanxl.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/14 0014 17:43
 */
@RequestMapping("/api")
@RestController
public class ApiController {

    @GetMapping("")
    public String get() {
        return "you get api information";
    }
}
