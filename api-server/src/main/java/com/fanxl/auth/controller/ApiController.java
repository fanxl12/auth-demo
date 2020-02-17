package com.fanxl.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String test() {
        return "you get api test";
    }
}
