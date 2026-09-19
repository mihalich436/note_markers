package com.easymarkersapp.easymarkersapp.config;
import com.easymarkersapp.easymarkersapp.model.User;
import com.easymarkersapp.easymarkersapp.service.JwtService;
import com.easymarkersapp.easymarkersapp.service.ProjectShareService;
import com.easymarkersapp.easymarkersapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import java.security.Principal;

@Component
public class AuthChannelInterceptor implements ChannelInterceptor {
    @Autowired
    private JwtService jwtTokenProvider;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectShareService shareService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            String shareToken = accessor.getFirstNativeHeader("X-Share-Token");

            User user = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                if (jwtTokenProvider.validateToken(jwt)) {
                    String userEmail = jwtTokenProvider.extractEmail(jwt);
                    user = userService.findByEmail(userEmail)
                            .orElseThrow(() -> new RuntimeException("Failed to find user by email"));
                }
            }

            Long shareProjectId = shareService.resolveProjectId(shareToken).orElse(null);

            if (user == null && shareProjectId == null) {
                throw new RuntimeException("Missing Authorization or X-Share-Token");
            }

            if (user != null) {
                accessor.setUser(new UserAuthentication(user, shareProjectId));
            } else {
                accessor.setUser(new ShareTokenAuthentication(shareProjectId, shareToken));
            }

            return message;
        }

        // SEND — только для залогиненных с явным доступом
        if (StompCommand.SEND.equals(accessor.getCommand())) {
            Principal p = accessor.getUser();
            if (p instanceof ShareTokenAuthentication) {
                throw new AccessDeniedException("Read-only share session cannot send messages");
            }
        }

        return message;
    }
}