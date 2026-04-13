package com.easymarkersapp.easymarkersapp.service;

import com.easymarkersapp.easymarkersapp.model.*;
import com.easymarkersapp.easymarkersapp.repository.MapRepository;
import com.easymarkersapp.easymarkersapp.repository.ProjectAccessRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MapService {
    @Autowired
    private MapRepository mapRepository;
    @Autowired
    private ProjectAccessRepository accessRepository;

    public List<Map> findByProjectId(Long projectId) {
        return mapRepository.findByProjectId(projectId);
    }
    public Optional<Map> findById(Long id) {
        return mapRepository.findById(id);
    }
    @Transactional
    public Map findByIdAndCheckAccess(Long id, User user) {
        Optional<Map> mapOptional = mapRepository.findById(id);
        if (mapOptional.isPresent()) {
            Map map = mapOptional.get();
            Project project = map.getProject();
//            if (project.getOwnerId().equals(user.getId())) {
//                System.out.println("Found by owner");
//                map.getMarkers();
//                return map;
//            }
            Optional<ProjectAccess> projectAccessOptional = accessRepository.findByProjectAndUser(project, user);
            if (projectAccessOptional.isPresent()) {
                System.out.println("Found project access");
                if (projectAccessOptional.get().getRole() == AccessRole.ADMIN) {
                    map.getMarkers();
                }
                else {
                    map.setMarkers(map.getMarkers().stream().filter(Marker::getVisibility).toList());
                }
                return map;
            }
        }
        return null;
    }
    public boolean existsByIdAndUser(Long id, User user) {
        Optional<Map> mapOptional = mapRepository.findById(id);
        if (mapOptional.isPresent()) {
            Map map = mapOptional.get();
            Project project = map.getProject();
            if (project.getOwnerId().equals(user.getId())) {
                System.out.println("Exists by owner");
                return true;
            }
            if (accessRepository.existsByProjectAndUser(project, user)) {
                System.out.println("Found project access");
                return true;
            }
        }
        return false;
    }
    public Optional<Map> findByIdAndProjectId(Long mapId, Long projectId) {
        return mapRepository.findByIdAndProjectId(mapId, projectId);
    }
    public Map save(Map map) {
        return mapRepository.save(map);
    }
}
