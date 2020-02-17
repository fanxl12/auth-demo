package com.fanxl.auth.filter;

import com.alibaba.fastjson.JSON;
import com.fanxl.auth.constant.AuthConstant;
import com.fanxl.auth.constant.AuthType;
import com.fanxl.auth.constant.RedirectType;
import com.fanxl.auth.properties.ClientProperties;
import com.fanxl.auth.properties.SecurityProperties;
import com.fanxl.auth.token.TokenInfo;
import com.fanxl.auth.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
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
 * @date: 2020/2/17 0017 20:35
 */
@Slf4j
@Component
@Order(3)
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private SecurityProperties securityProperties;

    private RestTemplate restTemplate = new RestTemplate();

    private String[] notAuthUrls = new String[] {"/favicon.ico", "/static", "/oauth/callback", "/api/test",
            "/api-server/favicon.ico", "/api-server/static", "/api-server/oauth/callback",
            "/api-server/api/test", "/api-server/api/cookies"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("requestSessionId:{}, sessionId:{}", request.getRequestedSessionId(), request.getSession().getId());
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("name:{}, value:{}", cookie.getName(), cookie.getValue());
            }
        }
        if(ArrayUtils.contains(notAuthUrls, request.getRequestURI())) {
            log.info("{}不需要认证", request.getRequestURI());
            filterChain.doFilter(request, response);
        } else {
            String accessToken = (String) request.getAttribute(AuthConstant.TOKEN_KEY);
            if (StringUtils.isEmpty(accessToken)) {
                String refreshToken = (String) request.getAttribute(AuthConstant.REFRESH_TOKEN_KEY);
                if (!StringUtils.isEmpty(refreshToken)) {
                    // 有刷新token，获取token
                    String oauthServiceUrl = securityProperties.getTokenServer();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    headers.setBasicAuth(securityProperties.getClient().getId(), securityProperties.getClient().getSecret());

                    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
                    params.add("grant_type", "refresh_token");
                    params.add("refresh_token", refreshToken);

                    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
                    try {
                        ResponseEntity<TokenInfo> responseEntity = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
                        log.info("刷新token了");
                        TokenInfo tokenInfo = responseEntity.getBody();
                        tokenInfo.init();
                        accessToken = tokenInfo.getAccess_token();

                        AuthType authType = AuthType.of(securityProperties.getType());
                        if (authType.equals(AuthType.COOKIE)) {
                            response.addCookie(CookieUtil.getCookie("fan_access_token", tokenInfo.getAccess_token(),
                                    tokenInfo.getExpires_in().intValue(), "web.fan.com"));

                            response.addCookie(CookieUtil.getCookie("fan_refresh_token", tokenInfo.getRefresh_token(),
                                    2592000, "web.fan.com"));
                        } else {
                            request.getSession().setAttribute("token", tokenInfo);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        toAuthLogin(response);
                    }
                }
            }
            if (!StringUtils.isEmpty(accessToken)) {
                request.setAttribute(AuthConstant.TOKEN_KEY, accessToken);
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
            RedirectType redirectType = RedirectType.of(securityProperties.getRedirectType());
            if (redirectType.equals(RedirectType.API)) {
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
