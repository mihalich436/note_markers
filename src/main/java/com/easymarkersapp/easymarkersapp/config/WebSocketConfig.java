package com.easymarkersapp.easymarkersapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private AuthChannelInterceptor authChannelInterceptor;

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        WebSocketMessageBrokerConfigurer.super.configureClientInboundChannel(registration);
        registration.interceptors(authChannelInterceptor);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Префикс для отправки от клиента
        config.setApplicationDestinationPrefixes("/app");

        // Префиксы для рассылки клиентам
        config.enableSimpleBroker("/topic"/*, "/queue"*/);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Эндпоинт для WebSocket с поддержкой SockJS (fallback)
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();

        // Отдельный эндпоинт для чистого WebSocket (без SockJS)
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }
}
