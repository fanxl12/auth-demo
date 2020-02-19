package com.fanxl.auth.filter;

import com.fanxl.auth.constant.RedirectType;
import com.fanxl.auth.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
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
 * @date: 2020/2/19 0019 14:49
 */
@Slf4j
@Component
@Order(4)
public class LogoutFilter extends OncePerRequestFilter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ServerProperties serverProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        filterChain.doFilter(request, response);

        String url = request.getRequestURI();
        String contextPath = serverProperties.getServlet().getContextPath();
        if (StringUtils.isNotEmpty(contextPath)) {
            url = url.substring(contextPath.length());
        }
        if (url.equals(securityProperties.getLogout() + "")) {
            try {
                RedirectType redirectType = RedirectType.of(securityProperties.getRedirectType());
                if (redirectType.equals(RedirectType.URL)) {
                    response.sendRedirect(securityProperties.getAuthServer() + "/logout?redirect_uri=" + securityProperties.getLogoutRedirectUrl());
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
