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
    private static final String testMap = """
            {
              "version": "1.0",
              "name": "Testport",
              "description": "This is a test map of a test town named Testport.",
              "timestamp": "2026-04-01T19:22:48.729Z",
              "imageUrl": "https://api.lazydmnotes.ru/uploads/test/test.1",
              "markers": [
                {
                  "id": 1774986744132,
                  "x": 41.26354978975309,
                  "y": 42.873372248189895,
                  "title": "Центр",
                  "note": "тест",
                  "description": "Огого,\\nэто же важная инфа. Ее очень важно прочесть целиком и полностью.\\nТак-то.",
                  "messages": [
                    {
                      "id": "1",
                      "author": "User",
                      "text": "тест",
                      "timestamp": "31.03.2026, 22:52:50"
                    },
                    {
                      "id": "2",
                      "author": "User",
                      "text": "здесь живут крутые челы, особенно дварфы",
                      "timestamp": "31.03.2026, 22:53:09"
                    },
                    {
                      "id": "3",
                      "author": "User",
                      "text": "так вот",
                      "timestamp": "31.03.2026, 22:53:30"
                    },
                    {
                      "id": "4",
                      "author": "User",
                      "text": "ааааааааааааааааааааааааааааааааааааааа",
                      "timestamp": "31.03.2026, 22:53:49"
                    },
                    {
                      "id": "5",
                      "author": "User",
                      "text": "newline",
                      "timestamp": "01.04.2026, 01:26:18"
                    },
                    {
                      "id": "6",
                      "author": "User",
                      "text": "with\\nnew line\\nwow",
                      "timestamp": "01.04.2026, 01:31:14"
                    },
                    {
                      "id": "7",
                      "author": "User",
                      "text": "test\\nnew line...",
                      "timestamp": "01.04.2026, 01:34:09"
                    },
                    {
                      "id": "8",
                      "author": "User",
                      "text": "а что если\\n\\nвот так",
                      "timestamp": "01.04.2026, 01:34:33"
                    }
                  ],
                  "color": "#424ef0",
                  "shape": "circle",
                  "size": 36,
                  "createdAt": "31.03.2026, 22:52:24",
                  "isUpdated": false,
                  "updatedAt": "01.04.2026, 01:20:22"
                },
                {
                  "id": 1774992558209,
                  "x": 61.55564307162356,
                  "y": 45.53920191077261,
                  "title": "еуые",
                  "note": "",
                  "description": "",
                  "createdAt": "01.04.2026, 00:29:18",
                  "isUpdated": false,
                  "updatedAt": "01.04.2026, 00:29:21",
                  "color": "#ef4444",
                  "shape": "circle",
                  "size": 36,
                  "messages": [
                    {
                      "id": "9",
                      "author": "User",
                      "text": "дарова",
                      "timestamp": "01.04.2026, 00:29:24"
                    },
                    {
                      "id": "10",
                      "author": "User",
                      "text": "надо протестить ссылки",
                      "timestamp": "01.04.2026, 00:29:27"
                    },
                    {
                      "id": "11",
                      "author": "User",
                      "text": "https://www.youtube.com/",
                      "timestamp": "01.04.2026, 00:37:10"
                    },
                    {
                      "id": "12",
                      "author": "User",
                      "text": "Заходите пж: https://www.youtube.com/",
                      "timestamp": "01.04.2026, 00:37:38"
                    }
                  ]
                }
              ],
              "settings": {
                "defaultShape": "circle",
                "defaultColor": "#ef4444",
                "defaultSize": 36,
                "showNotes": true,
                "minZoomForLabels": 1
              }
            }""";

    @Autowired
    private MapService mapService;
    @Autowired
    private MarkerService markerService;

    @GetMapping("test")
    public ResponseEntity<?> getTestMap() {
        return ResponseEntity.ok(testMap);
    }
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

        return ResponseEntity.status(404).body(new AuthResponse(null, null, "Cannot access map"));
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
        return ResponseEntity.status(404).body(new AuthResponse(null, null, "Cannot access map"));
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            String result = mapService.saveImage(id, currentUser, file);
            if (result == null) {
                return ResponseEntity.status(404)
                        .body(new AuthResponse(null, null, "Cannot access map"));
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
