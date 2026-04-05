package com.easymarkersapp.easymarkersapp.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/maps")
@CrossOrigin() //> add addr and port
public class MapController {
    private static final String testMap = """
            {
              "version": "1.0",
              "name": "Testpport",
              "description": "This is a test map of a test town named Testport.",
              "timestamp": "2026-04-01T19:22:48.729Z",
              "imageUrl": "https://s12nrg.storage.yandex.net/rdisk/4932cbd5f704d97192445eedda254a7163f2ca44dbe2104dbffd4604ead48b12/69d31752/U5lSaNESKFUE9PldC7iudBZokqE6KxGYrQu4khA3VEHSsHFYCrCfksWcg4uE6FpttDoHCaZKuiMcUi8HYjcoWw==?uid=227704591&filename=testport.png&disposition=inline&hash=&limit=0&content_type=image%2Fpng&owner_uid=227704591&fsize=2877405&hid=9eaaaf2337aa9efb3999b505310686b9&media_type=image&tknv=v3&etag=d1212fa3573257d76a8c1545376ad442&ts=64ec13e96f880&s=3ac882525b94da275f915ef2db20bf0877345d3de784f9e7fb37127ff3696d87&pb=U2FsdGVkX1_vb-xOWLioQkBsR82H6sn6zzT0dJJVeOQZUcJkVrgBBfSA9pSIUP2CFwJmMF8Za5tVB0KQOmoUAw16I1Uc7ul6qeMkkl4m20Q",
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

    @GetMapping("test")
    public String getTestMap() {
        return testMap;
    }
}
