package com.easymarkersapp.easymarkersapp.controller;

import com.easymarkersapp.easymarkersapp.dto.AuthResponse;
import com.easymarkersapp.easymarkersapp.dto.MarkerCreateRequest;
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
    public ResponseEntity<?> editMarker(@PathVariable Long id, @RequestBody MarkerCreateRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //> check user's role
        Marker marker = markerService.findByIdAndCheckAccess(id, currentUser);
        if (marker != null) {
            marker.update(request);
            Marker newMarker = markerService.save(marker);
            return ResponseEntity.ok(newMarker);
        }
        return ResponseEntity.status(404).body(new AuthResponse(null, null, "Cannot access map"));
    }
}
