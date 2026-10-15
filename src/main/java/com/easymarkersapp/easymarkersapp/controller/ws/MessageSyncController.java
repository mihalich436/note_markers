package com.easymarkersapp.easymarkersapp.controller.ws;

import com.easymarkersapp.easymarkersapp.dto.message.*;
import com.easymarkersapp.easymarkersapp.dto.ws.SyncResponse;
import com.easymarkersapp.easymarkersapp.model.*;
import com.easymarkersapp.easymarkersapp.service.MarkerService;
import com.easymarkersapp.easymarkersapp.service.MarkerMessageService;
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
    private MarkerMessageService markerMessageService;
    @Autowired
    private MarkerService markerService;
    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("map/{mapId}/markers/{markerId}/messages")
    public void addMessage(@DestinationVariable Long mapId,
                           @DestinationVariable Long markerId,
                           MessageSaveRequest request,
                           SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        Marker marker = markerService.findByIdAndCheckAccess(markerId, mapId, currentUser, AccessRole.CHAT);
        if (marker != null) {
            MarkerMessage messageToAdd = new MarkerMessage(request, markerId, currentUser.getId());
            //> second bd transaction
            MarkerMessage message = markerMessageService.save(messageToAdd);
            this.template.convertAndSend("/topic/map/" + marker.getMapId(),
                    new SyncResponse<>("message", "add", message));
        }
    }

    @MessageMapping("map/{mapId}/markers/{markerId}/messages/{messageId}/text")
    public void updateMessageText(@DestinationVariable Long mapId,
                                  @DestinationVariable Long markerId,
                                  @DestinationVariable Long messageId,
                                  MessageTextUpdateRequest request,
                                  SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        MarkerMessageUpdateResult result = markerMessageService.updateTextAndCheckAccess(
                messageId, markerId, mapId, request, currentUser);
        if (result != null) {
            this.template.convertAndSend("/topic/map/" + result.mapId(),
                    new SyncResponse<>("message", "upd", result.message()));
        }
    }

    @MessageMapping("map/{mapId}/markers/{markerId}/messages/{messageId}/visibility")
    public void updateMessageVisibility(@DestinationVariable Long mapId,
                                        @DestinationVariable Long markerId,
                                        @DestinationVariable Long messageId,
                                        MessageVisibilityUpdateRequest request,
                                        SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        MarkerMessageUpdateResult result = markerMessageService.updateVisibilityAndCheckAccess(
                messageId, markerId, mapId, request, currentUser);
        if (result != null) {
            this.template.convertAndSend("/topic/map/" + result.mapId(),
                    new SyncResponse<>("message", "upd", result.message()));
        }
    }

    @MessageMapping("map/{mapId}/markers/{markerId}/messages/{messageId}/delete")
    public void deleteMessage(@DestinationVariable Long mapId,
                              @DestinationVariable Long markerId,
                              @DestinationVariable Long messageId,
                              SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        if (markerMessageService.deleteByIdAndCheckAccess(messageId, markerId, mapId, currentUser)) {
            this.template.convertAndSend("/topic/map/" + mapId,
                    new SyncResponse<>("message", "del",
                            new MessageDeleteResponse(messageId, markerId)));
        }
    }
}