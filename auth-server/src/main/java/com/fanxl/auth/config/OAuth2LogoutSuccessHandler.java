package com.fanxl.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 退出成功之后的处理器
 */
@Slf4j
@Component
public class OAuth2LogoutSuccessHandler implements LogoutSuccessHandler {

	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.logout.LogoutSuccessHandler#onLogoutSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		String redirectUri = request.getParameter("redirect_uri");
		log.debug("退出成功，跳转到:{}", redirectUri);
		if(!StringUtils.isEmpty(redirectUri)) {
			response.sendRedirect(redirectUri);
		}
	}

}
