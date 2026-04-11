package com.easymarkersapp.easymarkersapp.controller;

import com.easymarkersapp.easymarkersapp.dto.AuthResponse;
import com.easymarkersapp.easymarkersapp.dto.MapCreateRequest;
import com.easymarkersapp.easymarkersapp.dto.MarkerCreateRequest;
import com.easymarkersapp.easymarkersapp.model.Map;
import com.easymarkersapp.easymarkersapp.model.Marker;
import com.easymarkersapp.easymarkersapp.model.User;
import com.easymarkersapp.easymarkersapp.service.MapService;
import com.easymarkersapp.easymarkersapp.service.MarkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
              "imageUrl": "https://psv4.userapi.com/s/v1/d2/Mgr97H-JIl_n8n0KyC5zLl9brQSlVPllbxVGD61mbT_Tl4pcvlUnEQp_5DwI9NQFnGtqy1COd7wH0RGNxLfYK8FZ-QWVijRcv37znoJrYnCylWY1fRsf0fB3NLIN7r11hUy8ybi7SpFX/testport.png",
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
    public String getTestMap() {
        return testMap;
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getMap(@PathVariable Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map map = mapService.findByIdAndCheckAccess(id, currentUser);
        if (map != null) {
            return ResponseEntity.ok(map);
        }

        return ResponseEntity.status(404).body(new AuthResponse(null, null, "Cannot access map"));
    }

    @PostMapping("/{id}/markers")
    public ResponseEntity<?> createMarker(@PathVariable Long id, @RequestBody MarkerCreateRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //> check user's role
        if (mapService.existsByIdAndUser(id, currentUser)) {
            Marker markerToAdd = new Marker(request);
            markerToAdd.setMapId(id);
            Marker marker = markerService.save(markerToAdd);
            return ResponseEntity.ok(marker);
        }
        return ResponseEntity.status(404).body(new AuthResponse(null, null, "Cannot access map"));
    }
}
