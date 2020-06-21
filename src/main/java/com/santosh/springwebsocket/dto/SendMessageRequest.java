package com.santosh.springwebsocket.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SendMessageRequest {

    private String to;
    private String message;
}
