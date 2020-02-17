/**
 * 
 */
package com.fanxl.auth.filter;

import com.fanxl.auth.constant.AuthConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description Cookie过滤器
 * @author: fanxl
 * @date: 2020/2/17 0017 20:35
 */
@Slf4j
@Component
@Order(1)
public class CookieTokenFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		log.info("cookie filter");
		String accessToken = getCookie(request, "fan_access_token");
		if (StringUtils.isNotBlank(accessToken)) {
			log.info("cookie 认证");
			request.setAttribute(AuthConstant.TOKEN_KEY, accessToken);
		} else {
			String refreshToken = getCookie(request, "fan_refresh_token");
			if (StringUtils.isNotBlank(refreshToken)) {
				request.setAttribute(AuthConstant.REFRESH_TOKEN_KEY, refreshToken);
			}
		}
		filterChain.doFilter(request, response);
	}

	private String getCookie(HttpServletRequest request, String name) {
		String result = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if(StringUtils.equals(name, cookie.getName())) {
					result = cookie.getValue();
					break;
				}
			}
		}
		return result;
	}


}
