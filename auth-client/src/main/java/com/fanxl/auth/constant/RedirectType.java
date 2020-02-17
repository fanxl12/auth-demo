package com.fanxl.auth.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * <h1>重定向类型</h1>
 * @author: fanxl
 */
@Getter
@AllArgsConstructor
public enum RedirectType {

    API("api", "直接返回302错误信息"),
    URL("url", "重定向到认证服务器");

    private String value;

    /** 规则描述 */
    private String description;

    /**
     * <h2>根据 code 获取到 AuthType</h2>
     * */
    public static RedirectType of(String value) {
        Objects.requireNonNull(value);

        return Stream.of(values())
                .filter(bean -> bean.value.equals(value))
                .findAny()
                .orElseThrow(
                        () -> new IllegalArgumentException(value + " not exists")
                );
    }
}
