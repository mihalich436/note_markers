package com.easymarkersapp.easymarkersapp.controller.rest;

import com.easymarkersapp.easymarkersapp.dto.AuthResponse;
import com.easymarkersapp.easymarkersapp.dto.map.MapWithRoleDTO;
import com.easymarkersapp.easymarkersapp.dto.marker.MarkerSaveRequest;
import com.easymarkersapp.easymarkersapp.exception.NotImageUploadedException;
import com.easymarkersapp.easymarkersapp.model.*;
import com.easymarkersapp.easymarkersapp.service.MapService;
import com.easymarkersapp.easymarkersapp.service.MarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/maps")
@CrossOrigin() //> add addr and port
public class MapController {
    @Autowired
    private MapService mapService;
    @Autowired
    private MarkerService markerService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getMap(@PathVariable Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MapWithRoleDTO mapDto = mapService.findByIdAndCheckAccess(id, currentUser);
        if (mapDto != null) {
            Map map = mapDto.getMap();
            if (map.getFile() && map.getFileVersion() != null && map.getFileVersion() > 0) {
                String fileUrl = "/uploads/" + (id%10) + "/" + id + "." + map.getFileVersion();
                map.setImageUrl(fileUrl);
            }
            return ResponseEntity.ok(mapDto);
        }

        return ResponseEntity.status(404).body("Cannot access map");
    }

    @PostMapping("/{id}/markers")
    public ResponseEntity<?> createMarker(@PathVariable Long id, @RequestBody MarkerSaveRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProjectAccess access = mapService.getRoleByIdAndUser(id, currentUser);
        if (access.getRole().hasAccess(AccessRole.EDITOR)) {
            Marker markerToAdd = new Marker(request);
            markerToAdd.setMapId(id);
            //> second transaction to db
            Marker marker = markerService.save(markerToAdd);
            return ResponseEntity.ok(marker);
        }
        return ResponseEntity.status(404).body("Cannot access map");
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            String result = mapService.saveImage(id, currentUser, file);
            if (result == null) {
                return ResponseEntity.status(404)
                        .body("Cannot access map");
            }
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload image");
        } catch (NotImageUploadedException e) {
            return ResponseEntity.badRequest().body("Only image files are allowed");
        }
    }
}
