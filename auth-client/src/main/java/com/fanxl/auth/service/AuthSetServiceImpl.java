package com.fanxl.auth.service;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/18 0018 20:10
 */
public class AuthSetServiceImpl implements AuthSetService {

    @Override
    public String[] getNotAuthUrl() {
        return new String[] {"/favicon.ico", "/static", "/oauth/callback"};
    }


}
