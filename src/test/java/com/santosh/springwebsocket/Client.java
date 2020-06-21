package com.santosh.springwebsocket;

import lombok.extern.slf4j.Slf4j;
import tech.gusavila92.websocketclient.WebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class Client {

    private WebSocketClient webSocketClient;

    public static void main(String[] args) {
        Client client = new Client();
        client.createWebSocketClient();
    }

    private void createWebSocketClient() {
        URI uri;
        try {
            uri = new URI("ws://localhost:9076/socket?name=subash&channel=web");
        } catch (URISyntaxException e) {
            log.error("Exception ", e);
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                log.info("onOpen");
                webSocketClient.send("Hello, World!");
            }

            @Override
            public void onTextReceived(String message) {
                log.info("onTextReceived");
            }

            @Override
            public void onBinaryReceived(byte[] data) {
                log.info("onBinaryReceived");
            }

            @Override
            public void onPingReceived(byte[] data) {
                log.info("onPingReceived");
            }

            @Override
            public void onPongReceived(byte[] data) {
                log.info("onPongReceived");
            }

            @Override
            public void onException(Exception e) {
                log.error(e.getMessage());
            }

            @Override
            public void onCloseReceived() {
                log.info("onCloseReceived");
            }
        };

        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.addHeader("Origin", "http://developer.example.com");
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }
}
