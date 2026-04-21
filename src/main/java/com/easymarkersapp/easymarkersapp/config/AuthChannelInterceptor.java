package com.easymarkersapp.easymarkersapp.config;
import com.easymarkersapp.easymarkersapp.filter.JwtAuthFilter;
import com.easymarkersapp.easymarkersapp.model.User;
import com.easymarkersapp.easymarkersapp.service.JwtService;
import com.easymarkersapp.easymarkersapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class AuthChannelInterceptor implements ChannelInterceptor {
    @Autowired
    private JwtService jwtTokenProvider; // Ваш сервис для работы с JWT
    @Autowired
    private UserService userService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println("preSend");
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            // Извлекаем токен из "родного" STOMP-заголовка
            final String authToken = accessor.getFirstNativeHeader("Authorization");

            if (authToken != null && authToken.startsWith("Bearer ")) {
                String jwt = authToken.substring(7);
                String userEmail = jwtTokenProvider.extractEmail(jwt); // Извлекаем логин из JWT

                if (userEmail != null && jwtTokenProvider.validateToken(jwt)) {
                    var userOpt = userService.findByEmail(userEmail);

                    if (userOpt.isPresent()) {
                        User user = userOpt.get();
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        accessor.setUser(auth);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                    else {
                        throw new RuntimeException("Failed to find user by email");
                    }
                } else {
                    throw new RuntimeException("Invalid or expired token");
                }
            } else {
                // Если токен не передан, можете либо пропустить (для анонимного доступа), либо кинуть исключение
                throw new RuntimeException("Missing Authorization header");
            }
        }
        return message;
    }
}