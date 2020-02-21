package com.fanxl.auth.filter;

import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/21 0021 12:31
 */
public class AppAuthenticationProcessFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_KEY = "Authorization";

    private TokenStore tokenStore;

    private ResourceServerTokenServices tokenServices;

    public AppAuthenticationProcessFilter(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public AppAuthenticationProcessFilter(ResourceServerTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    @Override
    public void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Authentication authentication = attemptAuthentication(request);
        if (Objects.nonNull(authentication)) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    /**
     * 验证权限
     *
     * @param request
     * @return
     * @throws AuthenticationException
     */
    private Authentication attemptAuthentication(HttpServletRequest request)
            throws AuthenticationException {
        String token = extractToken(request);
        if (!StringUtils.isBlank(token)) {
            if (Objects.nonNull(tokenStore)) {
                /** 直接访问tokenStore获取认证信息 */
                return tokenStore.readAuthentication(token);
            } else {
                try {
                    /** 访问认证服务器获取认证信息 */
                    return tokenServices.loadAuthentication(token);
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 先从请求头中获取token，没获取到就从请求体中获取
     *
     * @param request
     * @return
     */
    protected String extractToken(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION_KEY);
        while (headers.hasMoreElements()) {
            String value = parseAuthorizationValue(headers.nextElement());
            if (!StringUtils.isBlank(value)) {
                return value;
            }
        }
        // 如果没有从请求头中拿到token，就从请求体中获取
        return obtainRequestToken(request);
    }

    /**
     * 从请求体中获取token
     *
     * @param request
     * @return
     */
    private String obtainRequestToken(HttpServletRequest request) {
        String authorizationValue = request.getParameter(AUTHORIZATION_KEY);
        return parseAuthorizationValue(authorizationValue);
    }

    /**
     * 解析AuthorizationValue
     *
     * @param authorizationValue
     * @return
     */
    private String parseAuthorizationValue(String authorizationValue) {
        if (Objects.isNull(authorizationValue)) {
            return null;
        }
        if ((authorizationValue
                .toLowerCase()
                .startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase()))) {
            String authHeaderValue =
                    authorizationValue.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
            int commaIndex = authHeaderValue.indexOf(',');
            if (commaIndex > 0) {
                authHeaderValue = authHeaderValue.substring(0, commaIndex);
            }
            return authHeaderValue;
        }
        return null;
    }
}
