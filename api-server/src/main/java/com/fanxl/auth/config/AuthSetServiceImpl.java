package com.fanxl.auth.config;

import com.fanxl.auth.service.AuthSetService;
import org.springframework.stereotype.Component;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/18 0018 20:10
 */
@Component
public class AuthSetServiceImpl implements AuthSetService {

    @Override
    public String[] getNotAuthUrl() {
        return new String[] {"/favicon.ico", "/static", "/oauth/callback", "/api/test",
                "/api-server/favicon.ico", "/api-server/static", "/api-server/oauth/callback",
                "/api-server/api/test", "/api-server/api/cookies"};
    }


}
