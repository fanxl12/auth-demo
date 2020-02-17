/**
 * 
 */
package com.fanxl.auth.filter;

import com.alibaba.fastjson.JSON;
import com.fanxl.auth.properties.ClientProperties;
import com.fanxl.auth.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
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
 * @author jojo
 *
 */
@Slf4j
@Component
public class CookieTokenFilter extends OncePerRequestFilter {
	
//	private RestTemplate restTemplate = new RestTemplate();

	@Autowired
	private SecurityProperties securityProperties;

	private String[] notAuthUrls = new String[] {"/favicon.ico", "/static", "/oauth/callback", "/api/test",
			"/api-server/favicon.ico", "/api-server/static", "/api-server/oauth/callback", "/api-server/api/test"};

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

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//		RequestContext requestContext = RequestContext.getCurrentContext();
//		HttpServletRequest request = requestContext.getRequest();
//		!StringUtils.equals(request.getRequestURI(), "/logout");

		if(ArrayUtils.contains(notAuthUrls, request.getRequestURI())) {
			log.info("{}不需要认证", request.getRequestURI());
			filterChain.doFilter(request, response);
		} else {
			String accessToken = getCookie(request, "fan_access_token");
			boolean authed = false;
			if (StringUtils.isNotBlank(accessToken)) {
				authed = true;
			} else {
//				String refreshToken = getCookie(request, "fan_refresh_token");
//				if(StringUtils.isNotBlank(refreshToken)) {
//
//					String oauthServiceUrl = "http://gateway.imooc.com:9070/token/oauth/token";
//
//					HttpHeaders headers = new HttpHeaders();
//					headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//					headers.setBasicAuth("admin", "123456");
//
//					MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//					params.add("grant_type", "refresh_token");
//					params.add("refresh_token", refreshToken);
//
//					HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
//
//					try {
//						ResponseEntity<TokenInfo> newToken = restTemplate.exchange(oauthServiceUrl, HttpMethod.POST, entity, TokenInfo.class);
////					request.getSession().setAttribute("token", newToken.getBody().init());
//
//						requestContext.addZuulRequestHeader("Authorization", "bearer "+newToken.getBody().getAccess_token());
//
//						Cookie accessTokenCookie = new Cookie("imooc_access_token", newToken.getBody().getAccess_token());
//						accessTokenCookie.setMaxAge(newToken.getBody().getExpires_in().intValue());
//						accessTokenCookie.setDomain("imooc.com");
//						accessTokenCookie.setPath("/");
//						response.addCookie(accessTokenCookie);
//
//						Cookie refreshTokenCookie = new Cookie("imooc_refresh_token", newToken.getBody().getRefresh_token());
//						refreshTokenCookie.setMaxAge(2592000);
//						refreshTokenCookie.setDomain("imooc.com");
//						refreshTokenCookie.setPath("/");
//						response.addCookie(refreshTokenCookie);
//
//					} catch (Exception e) {
//						requestContext.setSendZuulResponse(false);
//						requestContext.setResponseStatusCode(500);
//						requestContext.setResponseBody("{\"message\":\"refresh fail\"}");
//						requestContext.getResponse().setContentType("application/json");
//					}
//
//				} else {
//					requestContext.setSendZuulResponse(false);
//					requestContext.setResponseStatusCode(500);
//					requestContext.setResponseBody("{\"message\":\"refresh fail\"}");
//					requestContext.getResponse().setContentType("application/json");
//				}
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
