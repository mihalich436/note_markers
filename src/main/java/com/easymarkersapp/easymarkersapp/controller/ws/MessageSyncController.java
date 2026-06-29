package com.easymarkersapp.easymarkersapp.controller.ws;

import com.easymarkersapp.easymarkersapp.dto.message.*;
import com.easymarkersapp.easymarkersapp.dto.ws.SyncResponse;
import com.easymarkersapp.easymarkersapp.model.*;
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

    @MessageMapping("messages/{id}/text")
    public void updateMessageText(MessageTextUpdateRequest request,
                              SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        MessageUpdateResult result = messageService.updateByIdAndCheckAccess(request.getMessageId(), request, currentUser);
        if (result != null) {
            this.template.convertAndSend("/topic/map/" + result.mapId(),
                    new SyncResponse<>("message", "upd", result.message()));
        }
    }

    @MessageMapping("messages/{id}/visibility")
    public void updateMessageVisibility(MessageVisibilityUpdateRequest request,
                                  SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        MessageUpdateResult result = messageService.updateByIdAndCheckAccess(request.getMessageId(), request, currentUser);
        if (result != null) {
            this.template.convertAndSend("/topic/map/" + result.mapId(),
                    new SyncResponse<>("message", "upd", result.message()));
        }
    }

    @MessageMapping("messages/{id}/delete")
    public void deleteMessage(@DestinationVariable Long id,
                             SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        MessageDeleteResult result = messageService.deleteByIdAndCheckAccess(id, currentUser);
        if (result != null) {
            this.template.convertAndSend("/topic/map/" + result.mapId(),
                    new SyncResponse<>("message", "del", new MessageDeleteResponse(id, result.markerId())));
        }
    }
}
