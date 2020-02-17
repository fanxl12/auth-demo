package com.fanxl.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/14 0014 17:36
 */
@Slf4j
@RestController
@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @GetMapping("/test")
    public String test(HttpServletRequest request, HttpServletResponse response) {
        log.info("requestSessionId:{}, sessionId:{}", request.getRequestedSessionId(), request.getSession().getId());
        if (request.getSession().getAttribute("test") != null) {
            log.info("设置session");
            request.getSession().setAttribute("test", "test");
        }
        return "you get api test";
    }

}
