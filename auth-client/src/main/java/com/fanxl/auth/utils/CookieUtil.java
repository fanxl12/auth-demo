package com.fanxl.auth.utils;

import com.fanxl.auth.constant.AuthConstant;
import com.fanxl.auth.constant.AuthType;
import com.fanxl.auth.properties.SecurityProperties;
import com.fanxl.auth.token.TokenInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/17 0017 21:19
 */
public class CookieUtil {

    /**
     * 创建Cookie对象
     * @param name 名称
     * @param value 值
     * @param maxAge 有效期 单位秒
     * @param url 域名
     * @return {@link Cookie} Cookie对象
     */
    public static Cookie getCookie(String name, String value, int maxAge, String url)  {
        Cookie cookie = new Cookie(name, value);
        try {
            URI uri = new URI(url);
            if (uri.getHost()!=null) {
                cookie.setDomain(uri.getHost());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        return cookie;
    }

    /**
     * token信息的存储
     * @param request
     * @param response
     * @param tokenInfo token信息
     * @param type {@link AuthType} 处理类型
     */
    public static void saveToken(HttpServletRequest request, HttpServletResponse response,
                                 TokenInfo tokenInfo, String type, String url) {
        AuthType authType = AuthType.of(type);
        if (authType.equals(AuthType.COOKIE)) {
            response.addCookie(CookieUtil.getCookie(AuthConstant.COOKIE_ACCESS_TOKEN_NAME, tokenInfo.getAccess_token(),
                    tokenInfo.getExpires_in().intValue(), url));

            response.addCookie(CookieUtil.getCookie(AuthConstant.COOKIE_REFRESH_TOKEN_NAME, tokenInfo.getRefresh_token(),
                    AuthConstant.COOKIE_REFRESH_TOKEN_TIME, url));
        } else {
            request.getSession().setAttribute(AuthConstant.SESSION_TOKEN_KEY, tokenInfo);
        }
    }

    public static TokenInfo getTokenInfo(SecurityProperties securityProperties, String code, String refreshToken,
                                         RestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(securityProperties.getClient().getId(), securityProperties.getClient().getSecret());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        if (StringUtils.isNotEmpty(code)) {
            params.add("code", code);
            params.add("grant_type", "authorization_code");
            params.add("redirect_uri", securityProperties.getClient().getCallback());
        } else {
            params.add("grant_type", "refresh_token");
            params.add("refresh_token", refreshToken);
        }
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        ResponseEntity<TokenInfo> responseEntity = restTemplate.exchange(securityProperties.getTokenServer() + AuthConstant.TOKEN_SERVER, HttpMethod.POST, entity, TokenInfo.class);
        TokenInfo tokenInfo = responseEntity.getBody();
        tokenInfo.init();
        return tokenInfo;
    }


}
