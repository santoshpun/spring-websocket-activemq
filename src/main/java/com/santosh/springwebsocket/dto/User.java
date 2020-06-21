package com.santosh.springwebsocket.dto;

import lombok.*;

import java.security.Principal;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User implements Principal {

    private String name;
    private String username;
    private String channel;
    private String sessionId;


}