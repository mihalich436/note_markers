package com.easymarkersapp.easymarkersapp.controller.ws;

import com.easymarkersapp.easymarkersapp.dto.MarkerMoveRequest;
import com.easymarkersapp.easymarkersapp.dto.MarkerMoveResponse;
import com.easymarkersapp.easymarkersapp.dto.MarkerSaveRequest;
import com.easymarkersapp.easymarkersapp.dto.ws.SyncResponse;
import com.easymarkersapp.easymarkersapp.model.AccessRole;
import com.easymarkersapp.easymarkersapp.model.Marker;
import com.easymarkersapp.easymarkersapp.model.ProjectAccess;
import com.easymarkersapp.easymarkersapp.model.User;
import com.easymarkersapp.easymarkersapp.service.MapService;
import com.easymarkersapp.easymarkersapp.service.MarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.util.Optional;

@Controller
public class MarkerSyncController {
    @Autowired
    private MarkerService markerService;
    @Autowired
    private MapService mapService;

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("map/{id}/markers")
    public void addMarker(@DestinationVariable Long id, MarkerSaveRequest request,
                          SimpMessageHeaderAccessor headerAccessor) {
        System.out.println("add marker");
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        ProjectAccess access = mapService.getRoleByIdAndUser(id, currentUser);
        System.out.println("Role: " + access.getRole().getDisplayName());
        if (access.getRole().hasAccess(AccessRole.EDITOR)) {
            System.out.println("Has access");
            Marker markerToAdd = new Marker(request);
            markerToAdd.setMapId(id);
            //> second transaction to db
            Marker marker = markerService.save(markerToAdd);
            this.template.convertAndSend("/topic/map/" + marker.getMapId(),
                    new SyncResponse<>("marker", "add", marker));
        }
    }

    @MessageMapping("markers/{id}")
    public void editMarker(@DestinationVariable Long id, MarkerSaveRequest request,
                           SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        Marker marker = markerService.updateByIdAndCheckAccess(request, id, currentUser, AccessRole.EDITOR);
        if (marker != null) {
            this.template.convertAndSend("/topic/map/" + marker.getMapId(),
                    new SyncResponse<>("marker", "upd", marker));
        }
    }

    @MessageMapping("markers/{id}/move")
    public void moveMarker(@DestinationVariable Long id, MarkerMoveRequest request,
                           SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        Marker marker = markerService.updateByIdAndCheckAccess(request, id, currentUser, AccessRole.EDITOR);
        if (marker != null) {
            this.template.convertAndSend("/topic/map/" + marker.getMapId(),
                    new SyncResponse<>("marker", "move", new MarkerMoveResponse(marker)));
        }
    }

    @MessageMapping("markers/{id}/delete")
    public void deleteMarker(@DestinationVariable Long id,
                           SimpMessageHeaderAccessor headerAccessor) {
        User currentUser = (User) ((UsernamePasswordAuthenticationToken) headerAccessor.getUser()).getPrincipal();
        Marker marker = markerService.findByIdAndCheckAccess(id, currentUser, AccessRole.EDITOR);
        if (marker != null) {
            markerService.deleteById(id);
            this.template.convertAndSend("/topic/map/" + marker.getMapId(),
                    new SyncResponse<>("marker", "del", id));
        }
    }
}
