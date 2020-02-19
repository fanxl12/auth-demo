package com.fanxl.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/14 0014 18:00
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 这里配置userDetailsService给刷新token使用的
        endpoints.userDetailsService(userDetailsService)
                .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("user-app")
                .secret(passwordEncoder.encode("123456"))
                .scopes("read", "write")
                .accessTokenValiditySeconds(3600)
                .resourceIds("user-server")
                .authorizedGrantTypes("password")
                .and()
                .withClient("api-app")
                .secret(passwordEncoder.encode("123456"))
                .scopes("read")
                .accessTokenValiditySeconds(60)
                .refreshTokenValiditySeconds(259200)
                .resourceIds("api-server")
                .redirectUris("http://web.fan.com:8010/api-server/oauth/callback")
                .autoApprove(true)
                .authorizedGrantTypes("authorization_code", "refresh_token")
                .and()
                .withClient("flow-app")
                .secret(passwordEncoder.encode("123456"))
                .scopes("read")
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(259200)
                .resourceIds("flow-server")
                .redirectUris("http://web.fan.com:8016/oauth/callback")
                .autoApprove(true)
                .authorizedGrantTypes("authorization_code")
                .and()
                .withClient("web-app")
                .secret(passwordEncoder.encode("123456"))
                .scopes("read")
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(259200)
                .resourceIds("web-server")
                .redirectUris("http://web.fan.com:8017/oauth/callback")
                .autoApprove(true)
                .authorizedGrantTypes("authorization_code");

    }

    /**
     * 配置什么过来进行token的验证，这里就是必须经过身份认证的请求
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("isAuthenticated()");
    }
}
