package com.fanxl.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * @description
 * @author: fanxl
 * @date: 2020/2/15 0015 21:18
 */
@Configuration
@ConfigurationProperties(prefix = "auth.security")
@EnableConfigurationProperties(SecurityProperties.class)
@Data
public class SecurityProperties {

    private ClientProperties client = new ClientProperties();

    private String authServer;

    private String tokenServer;

    private String type;

    private String redirectType;
}
