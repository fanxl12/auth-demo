package com.fanxl.auth.service;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/18 0018 20:05
 */
public interface AuthSetService {

    /**
     * 获取非认证的url地址
     * @return
     */
    String[] getNotAuthUrl();

}
