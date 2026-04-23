package com.easymarkersapp.easymarkersapp.service;

import com.easymarkersapp.easymarkersapp.dto.MapWithRoleDTO;
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
    public MapWithRoleDTO findByIdAndCheckAccess(Long id, User user) {
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
                ProjectAccess projectAccess = projectAccessOptional.get();
                if (projectAccess.getRole() == AccessRole.ADMIN) {
                    map.getMarkers();
                }
                else {
                    map.setMarkers(map.getMarkers().stream().filter(Marker::getVisibility).toList());
                }
                return new MapWithRoleDTO(map, projectAccess.getRole().name());
            }
        }
        return null;
    }
    @Transactional
    public Map findByIdAndProjectIdAndCheckRole(Long id, Long projectId, User user, AccessRole requiredRole) {
        Optional<Map> mapOptional = mapRepository.findByIdAndProjectId(id, projectId);
        if (mapOptional.isPresent()) {
            Map map = mapOptional.get();
            Project project = map.getProject();
            Optional<ProjectAccess> projectAccessOptional = accessRepository.findByProjectAndUser(project, user);
            if (projectAccessOptional.isPresent() && projectAccessOptional.get().getRole().hasAccess(requiredRole)) {
                return map;
            }
        }
        return null;
    }
    @Transactional
    public boolean existsByIdAndUser(Long id, User user) {
        Optional<Map> mapOptional = mapRepository.findById(id);
        if (mapOptional.isPresent()) {
            Map map = mapOptional.get();
            Project project = new Project(map.getProjectId());
            return accessRepository.existsByProjectAndUser(project, user);
//            if (project.getOwnerId().equals(user.getId())) {
//                System.out.println("Exists by owner");
//                return true;
//            }
        }
        return false;
    }
    @Transactional
    public ProjectAccess getRoleByIdAndUser(Long id, User user) {
        Optional<Map> mapOptional = mapRepository.findById(id);
        if (mapOptional.isPresent()) {
            Map map = mapOptional.get();
            Project project = new Project(map.getProjectId());
            return accessRepository.findByProjectAndUser(project, user).orElse(null);
        }
        return null;
    }
    public Optional<Map> findByIdAndProjectId(Long mapId, Long projectId) {
        return mapRepository.findByIdAndProjectId(mapId, projectId);
    }
    public Map save(Map map) {
        return mapRepository.save(map);
    }

    public void delete(Map map) {
        mapRepository.delete(map);
    }
}
