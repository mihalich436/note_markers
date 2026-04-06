package com.easymarkersapp.easymarkersapp.service;

import com.easymarkersapp.easymarkersapp.model.Map;
import com.easymarkersapp.easymarkersapp.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MapService {
    @Autowired
    private MapRepository mapRepository;

    public List<Map> findByProjectId(Long projectId) {
        return mapRepository.findByProjectId(projectId);
    }
    public Optional<Map> findById(Long id) {
        return mapRepository.findById(id);
    }
    public Optional<Map> findByIdAndProjectId(Long mapId, Long projectId) {
        return mapRepository.findByIdAndProjectId(mapId, projectId);
    }
    public Map save(Map map) {
        return mapRepository.save(map);
    }
}
