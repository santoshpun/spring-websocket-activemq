package com.santosh.springwebsocket.config;

import com.santosh.springwebsocket.handler.HttpHandshakeInterceptor;
import com.santosh.springwebsocket.handler.UserInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WsConfig implements WebSocketMessageBrokerConfigurer {

    public static final String ENDPOINT_CONNECT = "/socket";
    public static final String SUBSCRIBE_USER_PREFIX = "/private";
    public static final String SUBSCRIBE_USER_REPLY = "/reply";
    public static final String SUBSCRIBE_QUEUE = "/topic";

    @Autowired
    private ActivemqConfig activemqConfig;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry
                .enableStompBrokerRelay(SUBSCRIBE_QUEUE, SUBSCRIBE_USER_REPLY)
                .setRelayHost(activemqConfig.getHost())
                .setRelayPort(activemqConfig.getPort())
                .setClientLogin(activemqConfig.getUser())
                .setClientPasscode(activemqConfig.getPassword());

        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix(SUBSCRIBE_USER_PREFIX);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint(ENDPOINT_CONNECT)
                .addInterceptors(new HttpHandshakeInterceptor())
                .setAllowedOrigins("*");

        registry
                .addEndpoint(ENDPOINT_CONNECT)
                .addInterceptors(new HttpHandshakeInterceptor())
                .setAllowedOrigins("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new UserInterceptor());
    }

    //note: required for pesistent connection only when embeeded activemq used
    /*
    @Bean(initMethod = "start", destroyMethod = "stop")
    public BrokerService broker() throws Exception {
        final BrokerService broker = new BrokerService();
        broker.addConnector("stomp://" + activemqConfig.getHost() + ":" + activemqConfig.getPort());

        broker.setPersistent(true);
        final ManagementContext managementContext = new ManagementContext();
        managementContext.setCreateConnector(true);
        broker.setManagementContext(managementContext);
        return broker;
    }
         */
}
