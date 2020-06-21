package com.santosh.springwebsocket.handler;

import com.santosh.springwebsocket.dto.User;
import com.santosh.springwebsocket.service.UserStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Slf4j
@Component
public class SubscribeEventListener implements ApplicationListener<SessionSubscribeEvent> {

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor
                = MessageHeaderAccessor.getAccessor(event.getMessage(), StompHeaderAccessor.class);

//        if (accessor.getDestination().startsWith("/queue")) {
//            log.info("evt : " + event);
//
//            User user = (User) event.getMessage()
//                    .getHeaders()
//                    .get(SimpMessageHeaderAccessor.USER_HEADER);
//
//            log.info("User : " + user + " is successfully subscribed");
//
//            accessor.getSessionAttributes().put("user", user);
//
//            UserStore.getInstance().registerUser(user);
//        }
    }
}
