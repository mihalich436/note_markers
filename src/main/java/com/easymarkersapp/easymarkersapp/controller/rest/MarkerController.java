package com.easymarkersapp.easymarkersapp.controller.rest;

import com.easymarkersapp.easymarkersapp.dto.marker.MarkerSaveRequest;
import com.easymarkersapp.easymarkersapp.dto.marker.MarkerMoveRequest;
import com.easymarkersapp.easymarkersapp.dto.marker.MarkerMoveResponse;
import com.easymarkersapp.easymarkersapp.model.AccessRole;
import com.easymarkersapp.easymarkersapp.model.Marker;
import com.easymarkersapp.easymarkersapp.model.User;
import com.easymarkersapp.easymarkersapp.service.MarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/markers")
@CrossOrigin()
public class MarkerController {
    @Autowired
    private MarkerService markerService;
    @PostMapping("/{id}")
    public ResponseEntity<?> editMarker(@PathVariable Long id, @RequestBody MarkerSaveRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Marker marker = markerService.updateByIdAndCheckAccess(request, id, currentUser, AccessRole.EDITOR);
        if (marker != null) {
            return ResponseEntity.ok(marker);
        }
        return ResponseEntity.status(404).body( "Cannot access map");
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<?> moveMarker(@PathVariable Long id, @RequestBody MarkerMoveRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Marker marker = markerService.updateByIdAndCheckAccess(request, id, currentUser, AccessRole.EDITOR);
        if (marker != null) {
            return ResponseEntity.ok(new MarkerMoveResponse(marker));
        }
        return ResponseEntity.status(404).body("Cannot access map");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMarker(@PathVariable Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (markerService.deleteByIdAndCheckAccess(id, currentUser)) {
            return ResponseEntity.ok(id);
        }
        return ResponseEntity.status(404).body("Cannot access map");
    }
}
