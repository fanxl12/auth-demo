package com.fanxl.auth.filter;

import com.fanxl.auth.bean.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.thymeleaf.util.ArrayUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/3 0003 20:43
 */
@Slf4j
@Component
public class SessionTokenFilter extends OncePerRequestFilter {

    private RestTemplate restTemplate = new RestTemplate();

    private String[] notAuthUrls = new String[] {"/user/list", "/favicon.ico", "/static", "/oauth/callback"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("认证检测:{}", request.getRequestURI());
        if(ArrayUtils.contains(notAuthUrls, request.getRequestURI())) {
            filterChain.doFilter(request, response);
        } else {
            TokenInfo token = (TokenInfo)request.getSession().getAttribute("token");
            boolean authed = false;
            if(token != null) {
                String tokenValue = token.getAccess_token();
                if(token.isExpired()) {
                    String oauthServiceUrl = "http://auth.fan.com:8011/oauth/token";

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    headers.setBasicAuth("flow-app", "123456");

                    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                    params.add("grant_type", "refresh_token");
                    params.add("refresh_token", token.getRefresh_token());

                    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

                    try {
                        ResponseEntity<TokenInfo> newToken = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
                        log.info("刷新token了");
                        request.getSession().setAttribute("token", newToken.getBody().init());
                        tokenValue = newToken.getBody().getAccess_token();
                        authed = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        toAuthLogin(response);
                    }
                } else {
                    authed = true;
                }
            }
            if (authed) {
                filterChain.doFilter(request, response);
            } else {
                toAuthLogin(response);
            }
        }
    }

    private void toAuthLogin(HttpServletResponse response) {
        log.info("没有认证，要去认证服务器认证");
        String url = "http://auth.fan.com:8011/oauth/authorize?" +
                "client_id=flow-app&" +
                "redirect_uri=http://web.fan.com:8016/oauth/callback&" +
                "response_type=code&" +
                "state=/";
        try {
            response.sendRedirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
