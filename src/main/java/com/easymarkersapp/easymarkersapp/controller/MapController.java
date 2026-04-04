package com.easymarkersapp.easymarkersapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/map")
public class MapController {
    private static final String testMap = "{\n" +
            "  \"version\": \"1.0\",\n" +
            "  \"name\": \"Testpport\",\n" +
            "  \"description\": \"This is a test map of a test town named Testport.\",\n" +
            "  \"timestamp\": \"2026-04-01T19:22:48.729Z\",\n" +
            "  \"imageUrl\": \"https://4.downloader.disk.yandex.ru/preview/a3e92ac92e439d59024b9155bd38f41f70e6f1a3e617c6b0ac3711149ccc1834/inf/Ok0S6Gtb3VbuyG5IugaiFxN26r9KKgjOzmKsfZDJiBnmQaJTHW3-CnRcf0qEPsND2_jvKt1252boayAVmIju0w%3D%3D?uid=227704591&filename=testport.png&disposition=inline&hash=&limit=0&content_type=image%2Fpng&owner_uid=227704591&tknv=v3&size=1901x922\",\n" +
            "  \"markers\": [\n" +
            "    {\n" +
            "      \"id\": 1774986744132,\n" +
            "      \"x\": 41.26354978975309,\n" +
            "      \"y\": 42.873372248189895,\n" +
            "      \"title\": \"Центр\",\n" +
            "      \"note\": \"тест\",\n" +
            "      \"description\": \"Огого,\\nэто же важная инфа. Ее очень важно прочесть целиком и полностью.\\nТак-то.\",\n" +
            "      \"messages\": [\n" +
            "        {\n" +
            "          \"id\": \"1\",\n" +
            "          \"author\": \"User\",\n" +
            "          \"text\": \"тест\",\n" +
            "          \"timestamp\": \"31.03.2026, 22:52:50\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"2\",\n" +
            "          \"author\": \"User\",\n" +
            "          \"text\": \"здесь живут крутые челы, особенно дварфы\",\n" +
            "          \"timestamp\": \"31.03.2026, 22:53:09\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"3\",\n" +
            "          \"author\": \"User\",\n" +
            "          \"text\": \"так вот\",\n" +
            "          \"timestamp\": \"31.03.2026, 22:53:30\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"4\",\n" +
            "          \"author\": \"User\",\n" +
            "          \"text\": \"ааааааааааааааааааааааааааааааааааааааа\",\n" +
            "          \"timestamp\": \"31.03.2026, 22:53:49\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"5\",\n" +
            "          \"author\": \"User\",\n" +
            "          \"text\": \"newline\",\n" +
            "          \"timestamp\": \"01.04.2026, 01:26:18\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"6\",\n" +
            "          \"author\": \"User\",\n" +
            "          \"text\": \"with\\nnew line\\nwow\",\n" +
            "          \"timestamp\": \"01.04.2026, 01:31:14\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"7\",\n" +
            "          \"author\": \"User\",\n" +
            "          \"text\": \"test\\nnew line...\",\n" +
            "          \"timestamp\": \"01.04.2026, 01:34:09\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"8\",\n" +
            "          \"author\": \"User\",\n" +
            "          \"text\": \"а что если\\n\\nвот так\",\n" +
            "          \"timestamp\": \"01.04.2026, 01:34:33\"\n" +
            "        }\n" +
            "      ],\n" +
            "      \"color\": \"#424ef0\",\n" +
            "      \"shape\": \"circle\",\n" +
            "      \"size\": 36,\n" +
            "      \"createdAt\": \"31.03.2026, 22:52:24\",\n" +
            "      \"isUpdated\": false,\n" +
            "      \"updatedAt\": \"01.04.2026, 01:20:22\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 1774992558209,\n" +
            "      \"x\": 61.55564307162356,\n" +
            "      \"y\": 45.53920191077261,\n" +
            "      \"title\": \"еуые\",\n" +
            "      \"note\": \"\",\n" +
            "      \"description\": \"\",\n" +
            "      \"createdAt\": \"01.04.2026, 00:29:18\",\n" +
            "      \"isUpdated\": false,\n" +
            "      \"updatedAt\": \"01.04.2026, 00:29:21\",\n" +
            "      \"color\": \"#ef4444\",\n" +
            "      \"shape\": \"circle\",\n" +
            "      \"size\": 36,\n" +
            "      \"messages\": [\n" +
            "        {\n" +
            "          \"id\": \"9\",\n" +
            "          \"author\": \"User\",\n" +
            "          \"text\": \"дарова\",\n" +
            "          \"timestamp\": \"01.04.2026, 00:29:24\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"10\",\n" +
            "          \"author\": \"User\",\n" +
            "          \"text\": \"надо протестить ссылки\",\n" +
            "          \"timestamp\": \"01.04.2026, 00:29:27\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"11\",\n" +
            "          \"author\": \"User\",\n" +
            "          \"text\": \"https://www.youtube.com/\",\n" +
            "          \"timestamp\": \"01.04.2026, 00:37:10\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"id\": \"12\",\n" +
            "          \"author\": \"User\",\n" +
            "          \"text\": \"Заходите пж: https://www.youtube.com/\",\n" +
            "          \"timestamp\": \"01.04.2026, 00:37:38\"\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ],\n" +
            "  \"settings\": {\n" +
            "    \"defaultShape\": \"circle\",\n" +
            "    \"defaultColor\": \"#ef4444\",\n" +
            "    \"defaultSize\": 36,\n" +
            "    \"showNotes\": true,\n" +
            "    \"minZoomForLabels\": 1\n" +
            "  }\n" +
            "}";

    @GetMapping("test")
    public String getTestMap() {
        return testMap;
    }
}
