package com.santosh.springwebsocket.handler;

import com.santosh.springwebsocket.dto.User;
import com.santosh.springwebsocket.service.UserStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

@Slf4j
public class UserInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor
                = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        log.info("accessor headers : " + accessor);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.info("************ STOMP COMMAND *****" + accessor.getCommand());

            String username = (String) accessor.getSessionAttributes().get("username");
            log.info("username : {}", username);

            String channelCode = (String) accessor.getSessionAttributes().get("channel");
            String destination = accessor.getDestination();

            log.info("Event {}", message.toString());

            Object simSessionId = message
                    .getHeaders()
                    .get(SimpMessageHeaderAccessor.SESSION_ID_HEADER);

            String name = username + channelCode;

            User user = new User(name, username, channelCode, (String) simSessionId);

            accessor.setUser(user);

            accessor.getSessionAttributes().put("user", user);

            UserStore.getInstance().registerUser(user);
        }
        return message;
    }
}