package com.santosh.springwebsocket.service;

import com.santosh.springwebsocket.config.WsConfig;
import com.santosh.springwebsocket.dto.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@SuppressWarnings({"Duplicates"})
@Component
public class MessageSender {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    public void sendToAllConnectedUsers(String from) {
        log.info("Sending msg by : " + from);

        final String time = new SimpleDateFormat("HH:mm").format(new Date());

        ChatMessage textMessage = new ChatMessage();
        textMessage.setMessage("Hallo to all - at time : " + time);

        messagingTemplate.convertAndSend(WsConfig.SUBSCRIBE_QUEUE, textMessage);
    }

    public void sendMessageToUser(String to, String message) {
        log.info("Sending msg to : " + to);

        final String time = new SimpleDateFormat("HH:mm").format(new Date());

        ChatMessage textMessage = new ChatMessage();
        textMessage.setMessage(message + " - at time : " + time);

        messagingTemplate.convertAndSendToUser(to, WsConfig.SUBSCRIBE_USER_REPLY, textMessage);
    }
}
