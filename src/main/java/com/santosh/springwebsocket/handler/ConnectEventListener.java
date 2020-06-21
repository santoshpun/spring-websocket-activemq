package com.santosh.springwebsocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Slf4j
@Component
public class ConnectEventListener implements ApplicationListener<SessionConnectedEvent> {

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        log.info("New connection received.");

        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

        System.out.println("StompConnectEvent::onApplicationEvent()    sha.getSessionId(): " + sha.getSessionId() +
                " sha.toNativeHeaderMap():" + sha.toNativeHeaderMap());
    }
}
