package com.fanxl.auth.filter;

import com.alibaba.fastjson.JSON;
import com.fanxl.auth.properties.ClientProperties;
import com.fanxl.auth.properties.SecurityProperties;
import com.fanxl.auth.token.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/3 0003 20:43
 */
@Slf4j
@Component
public class SessionTokenFilter extends OncePerRequestFilter {

    @Autowired
    private SecurityProperties securityProperties;

    private RestTemplate restTemplate = new RestTemplate();

    private String[] notAuthUrls = new String[] {"/favicon.ico", "/static", "/oauth/callback", "/api/test"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("requestSessionId:{}, sessionId:{}", request.getRequestedSessionId(), request.getSession().getId());
        if(ArrayUtils.contains(notAuthUrls, request.getRequestURI())) {
            log.info("{}不需要认证", request.getRequestURI());
            filterChain.doFilter(request, response);
        } else {
            log.info("认证检测:{}", request.getRequestURI());
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    log.info("name:{}, value:{}", cookie.getName(), cookie.getValue());
                }
            }
            TokenInfo token = (TokenInfo)request.getSession().getAttribute("token");
            boolean authed = false;
            if(token != null) {
                String tokenValue = token.getAccess_token();
                if(token.isExpired()) {
                    String oauthServiceUrl = securityProperties.getTokenServer();

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    headers.setBasicAuth(securityProperties.getClient().getId(), securityProperties.getClient().getSecret());

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
                log.info("token:{}", tokenValue);
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
        ClientProperties client = securityProperties.getClient();
        String url = securityProperties.getAuthServer() + "?" +
                "client_id=" + client.getId() + "&" +
                "redirect_uri=" + client.getCallback() + "&" +
                "response_type=code&" +
                "state=" + client.getState();

        try {
            if ("token".equalsIgnoreCase(securityProperties.getType()+ "")) {
                response.setContentType("application/json");
                response.setStatus(HttpStatus.FOUND.value());

                Map<String, String> map = new HashMap<>();
                map.put("message", "auth fail");
                map.put("redirectUrl", url);
                response.getWriter().write(JSON.toJSONString(map));
            } else {
                response.sendRedirect(url);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
