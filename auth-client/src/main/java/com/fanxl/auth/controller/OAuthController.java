package com.fanxl.auth.controller;

import com.fanxl.auth.properties.SecurityProperties;
import com.fanxl.auth.token.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
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

        String oauthServiceUrl = securityProperties.getTokenServer();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(securityProperties.getClient().getId(), securityProperties.getClient().getSecret());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", securityProperties.getClient().getCallback());

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<TokenInfo> responseEntity = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
        TokenInfo tokenInfo = responseEntity.getBody();
        tokenInfo.init();
        log.info("token: {}", tokenInfo.toString());

//        request.getSession().setAttribute("token", tokenInfo);

        Cookie accessTokenCookie = new Cookie("fan_access_token", tokenInfo.getAccess_token());
        accessTokenCookie.setMaxAge(tokenInfo.getExpires_in().intValue());
        accessTokenCookie.setDomain("vue.fxl.com");
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

//        Cookie refreshTokenCookie = new Cookie("fan_refresh_token", tokenInfo.getRefresh_token());
//        refreshTokenCookie.setMaxAge(2592000);
//        refreshTokenCookie.setDomain("localhost");
//        refreshTokenCookie.setPath("/");
//        response.addCookie(refreshTokenCookie);

        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Headers", "http://vue.fxl.com:8080");
        response.addHeader("Access-Control-Allow-Headers", "Authorization,Origin, X-Requested-With, Content-Type, Accept,Access-Token, Cookies");
        response.addHeader("Access-Control-Max-Age", "3600");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");

        response.sendRedirect(state);
    }
}
