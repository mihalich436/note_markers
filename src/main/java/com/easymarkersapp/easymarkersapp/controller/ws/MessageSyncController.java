package com.easymarkersapp.easymarkersapp.controller.ws;

import com.easymarkersapp.easymarkersapp.dto.marker.MarkerSaveRequest;
import com.easymarkersapp.easymarkersapp.dto.message.MessageSaveRequest;
import com.easymarkersapp.easymarkersapp.dto.ws.SyncResponse;
import com.easymarkersapp.easymarkersapp.model.*;
import com.easymarkersapp.easymarkersapp.service.MapService;
import com.easymarkersapp.easymarkersapp.service.MarkerService;
import com.easymarkersapp.easymarkersapp.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

@Controller
public class MessageSyncController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private MarkerService markerService;
    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("messages")
    public void addMessage(MessageSaveRequest request,
                           SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        Marker marker = markerService.findByIdAndCheckAccess(request.getMarkerId(), currentUser, AccessRole.CHAT);
        if (marker != null) {
            Message messageToAdd = new Message(request, currentUser.getId());
            //> second bd transaction
            Message message = messageService.save(messageToAdd);
            this.template.convertAndSend("/topic/map/" + marker.getMapId(),
                    new SyncResponse<>("message", "add", message));
        }
    }
}
