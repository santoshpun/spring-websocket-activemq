package com.santosh.springwebsocket.controller;

import com.santosh.springwebsocket.config.WsConfig;
import com.santosh.springwebsocket.dto.*;
import com.santosh.springwebsocket.service.MessageSender;
import com.santosh.springwebsocket.service.UserStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("user/session")
public class UserSessionController {
    @Autowired
    private MessageSender messageSender;
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @GetMapping(value = "connected", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getConnectedUserList() {
        List<User> users = UserStore.getInstance().getUsers();

        List<User> userListResponse = new ArrayList<>();

        for (User user : users) {
            userListResponse.add(user);
        }
        return new ResponseEntity<>(userListResponse, HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendMessageToUser(@RequestBody SendMessageRequest request) {

        messageSender.sendMessageToUser(request.getTo(), request.getMessage());

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSuccess(true);
        baseResponse.setMessage("Message Sent");

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @MessageMapping("/payment")
    @SendTo("/topic/payment")
    public UserResponse greeting(NameDTO nameDTO) throws Exception {
        return new UserResponse("Hi " + nameDTO.getName());
    }

    @PostMapping(value = "send", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> send(@RequestBody SendMessageRequest request) {

        messagingTemplate.convertAndSendToUser(request.getTo(), WsConfig.SUBSCRIBE_USER_REPLY, request.getMessage());

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSuccess(true);
        baseResponse.setMessage("Message Sent");

        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

    @PostConstruct
    public void init() {
        ScheduledExecutorService statusTimerExecutor = Executors.newSingleThreadScheduledExecutor();

        statusTimerExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                messagingTemplate.convertAndSendToUser("santoshweb", WsConfig.SUBSCRIBE_USER_REPLY, new UserResponse("test" + new Date()));
            }
        }, 5000, 5000, TimeUnit.MILLISECONDS);
    }
}
