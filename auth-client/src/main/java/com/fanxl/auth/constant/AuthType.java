package com.fanxl.auth.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>认证类型</h1>
 * @author: fanxl
 */
@Getter
@AllArgsConstructor
public enum AuthType {

    SESSION("session", "session类型，服务器保存认证信息"),
    COOKIE("cookie", "cookie类型，客户端保存认证信息");

    private String value;

    /** 规则描述 */
    private String description;

    /**
     * <h2>根据 code 获取到 AuthType</h2>
     * */
    public static AuthType of(String value) {
        Objects.requireNonNull(value);

        return Stream.of(values())
                .filter(bean -> bean.value.equals(value))
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException(value + " not exists")
                );
    }
}
