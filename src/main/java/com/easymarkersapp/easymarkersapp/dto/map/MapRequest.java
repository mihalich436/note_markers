package com.easymarkersapp.easymarkersapp.dto.map;

import com.easymarkersapp.easymarkersapp.model.Map;

public interface MapRequest {
    // Возвращает true при необходимости удаления файла
    boolean updateMap(Map map);
}
