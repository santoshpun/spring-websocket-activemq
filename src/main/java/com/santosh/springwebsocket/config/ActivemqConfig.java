package com.santosh.springwebsocket.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "activemq.broker")
public class ActivemqConfig {
    private String host;
    private Integer port;
    private String user;
    private String password;
}
