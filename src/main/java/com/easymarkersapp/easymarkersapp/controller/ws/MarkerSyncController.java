package com.easymarkersapp.easymarkersapp.controller.ws;

import com.easymarkersapp.easymarkersapp.dto.marker.MarkerMoveRequest;
import com.easymarkersapp.easymarkersapp.dto.marker.MarkerMoveResponse;
import com.easymarkersapp.easymarkersapp.dto.marker.MarkerSaveRequest;
import com.easymarkersapp.easymarkersapp.dto.ws.SyncResponse;
import com.easymarkersapp.easymarkersapp.model.AccessRole;
import com.easymarkersapp.easymarkersapp.model.Marker;
import com.easymarkersapp.easymarkersapp.model.ProjectAccess;
import com.easymarkersapp.easymarkersapp.model.User;
import com.easymarkersapp.easymarkersapp.service.MapService;
import com.easymarkersapp.easymarkersapp.service.MarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

@Controller
public class MarkerSyncController {
    @Autowired
    private MarkerService markerService;
    @Autowired
    private MapService mapService;

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("map/{mapId}/markers")
    public void addMarker(@DestinationVariable Long mapId, MarkerSaveRequest request,
                          SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        ProjectAccess access = mapService.getRoleByIdAndUser(mapId, currentUser);
        if (access.getRole().hasAccess(AccessRole.EDITOR)) {
            Marker markerToAdd = new Marker(request);
            markerToAdd.setMapId(mapId);
            //> second transaction to db
            Marker marker = markerService.save(markerToAdd);
            this.template.convertAndSend("/topic/map/" + marker.getMapId(),
                    new SyncResponse<>("marker", "add", marker));
        }
    }

    @MessageMapping("map/{mapId}/markers/{markerId}")
    public void editMarker(@DestinationVariable Long mapId,
                           @DestinationVariable Long markerId,
                           MarkerSaveRequest request,
                           SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        // id в теле запроса больше не используется — маркер идентифицируется парой (mapId, markerId) из URL
        Marker marker = markerService.updateByIdAndCheckAccess(markerId, mapId, request, currentUser, AccessRole.EDITOR);
        if (marker != null) {
            this.template.convertAndSend("/topic/map/" + marker.getMapId(),
                    new SyncResponse<>("marker", "upd", marker));
        }
    }

    @MessageMapping("map/{mapId}/markers/{markerId}/move")
    public void moveMarker(@DestinationVariable Long mapId,
                           @DestinationVariable Long markerId,
                           MarkerMoveRequest request,
                           SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        Marker marker = markerService.updateByIdAndCheckAccess(markerId, mapId, request, currentUser, AccessRole.EDITOR);
        if (marker != null) {
            this.template.convertAndSend("/topic/map/" + marker.getMapId(),
                    new SyncResponse<>("marker", "move", new MarkerMoveResponse(marker)));
        }
    }

    @MessageMapping("map/{mapId}/markers/{markerId}/copy")
    public void copyMarker(@DestinationVariable Long mapId,
                           @DestinationVariable Long markerId,
                           MarkerMoveRequest request,
                           SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        Marker marker = markerService.copyAndCheckAccess(request, markerId, mapId, currentUser, AccessRole.EDITOR);
        if (marker != null) {
            this.template.convertAndSend("/topic/map/" + marker.getMapId(),
                    new SyncResponse<>("marker", "add", marker));
        }
    }

    @MessageMapping("map/{mapId}/markers/{markerId}/delete")
    public void deleteMarker(@DestinationVariable Long mapId,
                             @DestinationVariable Long markerId,
                             SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        Marker marker = markerService.findByIdAndCheckAccess(markerId, mapId, currentUser, AccessRole.EDITOR);
        if (marker != null) {
            markerService.deleteById(markerId);
            this.template.convertAndSend("/topic/map/" + marker.getMapId(),
                    new SyncResponse<>("marker", "del", markerId));
        }
    }
}