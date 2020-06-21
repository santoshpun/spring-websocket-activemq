package com.santosh.springwebsocket.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BaseResponse {

    private boolean success;
    private String message;
}
