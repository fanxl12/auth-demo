package com.fanxl.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/19 0019 11:56
 */
@Slf4j
public class MyErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        HttpStatus httpStatus = response.getStatusCode();
        return httpStatus.series() == HttpStatus.Series.CLIENT_ERROR ||
                httpStatus.series() == HttpStatus.Series.SERVER_ERROR;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        log.info("请求出错了");
    }
}
