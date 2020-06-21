package com.santosh.springwebsocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
public class ClientRunner {

    private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

    public static void main(String[] args) throws Exception {
        ClientRunner clientRunner = new ClientRunner();

        ListenableFuture<StompSession> f = clientRunner.connect();
        StompSession stompSession = f.get();

        log.info("Subscribing to greeting topic using session " + stompSession);
        clientRunner.subscribe(stompSession);

        Thread.sleep(6000000);
    }

    public ListenableFuture<StompSession> connect() {

        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);

        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());

        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        String url = "http://localhost:9076/socket?name=santosh&channel=web";

        return stompClient.connect(url, headers, new MyHandler());
    }

    public void subscribe(StompSession stompSession) throws ExecutionException, InterruptedException {
        stompSession.subscribe("/private/reply", new StompFrameHandler() {

            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers,
                                    Object payload) {
                log.info("Received msg : " + payload.toString());
            }
        });
    }

    private class MyHandler extends StompSessionHandlerAdapter {
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            log.info("Now connected");
            log.info("headers : " + stompHeaders);
        }
    }

    public void showHeaders(StompHeaders headers) {
        for (Map.Entry<String, List<String>> e : headers.entrySet()) {
            log.info("  " + e.getKey() + ": ");
            boolean first = true;
            for (String v : e.getValue()) {
                if (!first) log.info(", ");
                log.info(v);
                first = false;
            }
            log.info("");
        }
    }
}
