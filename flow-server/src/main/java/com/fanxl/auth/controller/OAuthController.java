package com.fanxl.auth.controller;

import com.fanxl.auth.bean.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

    @GetMapping("callback")
    public void callback(@RequestParam String code,
                       String state,
                       HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        log.info("state is "+state);

        String oauthServiceUrl = "http://auth.fan.com:8011/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth("flow-app", "123456");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", "http://web.fan.com:8016/oauth/callback");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<TokenInfo> responseEntity = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
        TokenInfo tokenInfo = responseEntity.getBody();
        tokenInfo.init();
        log.info("token: {}", tokenInfo.toString());
        request.getSession().setAttribute("token", tokenInfo);
        response.sendRedirect(state);
    }
}
