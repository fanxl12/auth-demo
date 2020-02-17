package com.fanxl.auth.filter;

import com.fanxl.auth.constant.AuthConstant;
import com.fanxl.auth.token.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
@Order(2)
public class SessionTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("session filter");
        TokenInfo token = (TokenInfo)request.getSession().getAttribute("token");
        if(token != null) {
            String accessToken = token.getAccess_token();
            if(token.isExpired()) {
                request.setAttribute(AuthConstant.REFRESH_TOKEN_KEY, token.getRefresh_token());
            } else {
                log.info("session 认证");
                request.setAttribute(AuthConstant.TOKEN_KEY, accessToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
