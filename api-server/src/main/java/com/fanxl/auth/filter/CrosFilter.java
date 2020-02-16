package com.fanxl.auth.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description 跨域处理的Filter
 * @author: fanxl
 * @date: 2018/10/10 0010 12:42
 */
@Slf4j
public class CrosFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 如果是所有方法和所有域名 可以使用 *
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
//        response.addHeader("Access-Control-Allow-Headers", "Content-Type,X-CAF-Authorization-Token,sessionToken,X-TOKEN,X-Requested-With");
        response.addHeader("Access-Control-Allow-Headers", "Authorization,Origin, X-Requested-With, Content-Type, Accept,Access-Token, Cookies");
        response.addHeader("Access-Control-Max-Age", "3600");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info("跨域: {}", request.getRequestURI());
        String origin = request.getHeader("Origin");
        if (!StringUtils.isEmpty(origin)){
            log.info("设置:{}", origin);
            response.addHeader("Access-Control-Allow-Origin", origin);
        }
        // 支持cookie
        response.addHeader("Access-Control-Allow-Credentials", "true");

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.info("name:{}, value:{}", cookie.getName(), cookie.getValue());
            }
        }

        // 支持所有自定义头的跨域
        String heads = request.getHeader("Access-Control-Allow-Headers");
        if (!StringUtils.isEmpty(heads)) {
            response.addHeader("Access-Control-Allow-Headers", heads);
        }
        if (request.getMethod().equals("OPTIONS")) {
            response.getWriter().println("ok");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
