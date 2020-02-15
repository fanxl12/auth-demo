package com.fanxl.auth.properties;

import lombok.Data;

/**
 * @description
 * @author: fanxl
 * @date: 2020/2/15 0015 21:20
 */
@Data
public class ClientProperties {

    private String id;

    private String secret;

    private String callback;

    private String state;

}
