package com.santosh.springwebsocket.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.messaging.handler.annotation.SendTo;

@Getter
@SendTo
@ToString
public class NameDTO {
    private String name;
}
