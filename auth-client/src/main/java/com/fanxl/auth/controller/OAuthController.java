package com.fanxl.auth.controller;

import com.fanxl.auth.properties.SecurityProperties;
import com.fanxl.auth.token.TokenInfo;
import com.fanxl.auth.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/14 0014 21:03
 */
@Slf4j
@RequestMapping("/oauth")
@RestController
public class OAuthController {

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private SecurityProperties securityProperties;

    @GetMapping("callback")
    public void callback(@RequestParam String code,
                       String state,
                       HttpServletRequest request,
                       HttpServletResponse response) throws Exception {

        log.info("state is " + state);
        TokenInfo tokenInfo = CookieUtil.getTokenInfo(securityProperties, code, null, restTemplate);
        log.info("token: {}", tokenInfo.toString());

        CookieUtil.saveToken(request, response, tokenInfo, securityProperties.getType(), state);

        response.sendRedirect(state);
    }
}
