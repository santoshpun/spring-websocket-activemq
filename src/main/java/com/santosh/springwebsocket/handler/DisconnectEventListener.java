package com.santosh.springwebsocket.handler;

import com.santosh.springwebsocket.dto.User;
import com.santosh.springwebsocket.service.UserStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
public class DisconnectEventListener implements ApplicationListener<SessionDisconnectEvent> {

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
        log.info("evt : " + event);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        User user = (User) headerAccessor.getSessionAttributes().get("user");

        log.info("User : " + user + " has successfully disconnected");

        UserStore.getInstance().unregisterUser(user);
    }
}
