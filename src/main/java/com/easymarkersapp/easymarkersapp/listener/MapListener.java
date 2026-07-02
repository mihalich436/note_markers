package com.easymarkersapp.easymarkersapp.listener;

import com.easymarkersapp.easymarkersapp.contextHolder.SpringContextHolder;
import com.easymarkersapp.easymarkersapp.model.Map;
import com.easymarkersapp.easymarkersapp.service.MapService;
import jakarta.persistence.PreRemove;

public class MapListener {
    @PreRemove
    public void onDelete(Map map) {
        // Получаем сервис через SpringContextHolder
        MapService mapService = SpringContextHolder.getBean(MapService.class);

        // Удаляем файл, если он есть
        if (map.getFile()) {
            mapService.deleteImage(map);
        }
    }
}
