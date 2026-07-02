package com.easymarkersapp.easymarkersapp.service;

import com.easymarkersapp.easymarkersapp.dto.map.MapWithRoleDTO;
import com.easymarkersapp.easymarkersapp.exception.NotImageUploadedException;
import com.easymarkersapp.easymarkersapp.model.*;
import com.easymarkersapp.easymarkersapp.repository.MapRepository;
import com.easymarkersapp.easymarkersapp.repository.ProjectAccessRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

@Service
public class MapService {
    @Autowired
    private MapRepository mapRepository;
    @Autowired
    private ProjectAccessRepository accessRepository;
    @Value("${file.upload-dir}")
    private String uploadDir;

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
    public Map findByIdAndCheckRole(Long id, User user, AccessRole requiredRole) {
        Optional<Map> mapOptional = mapRepository.findById(id);
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
    public void deleteImage(Map map) {
        String filenamePrev = map.getId() + "." + map.getFileVersion();
        Path uploadPath = Paths.get(uploadDir + "/" + (map.getId() % 10));
        try {
            Path filePathPrev = uploadPath.resolve(filenamePrev);
            Files.delete(filePathPrev);
        }
        catch (IOException ignored) {}
    }
    @Transactional
    public String saveImage(Long id, User user, MultipartFile file) throws NotImageUploadedException, IOException {
        Map map = findByIdAndCheckRole(id, user, AccessRole.ADMIN);
        if (map != null) {
            // Проверка типа файла
            String contentType = file.getContentType();
            if (!contentType.startsWith("image/")) {
                throw new NotImageUploadedException();
            }

            int prevVersion = map.getFileVersion() == null ? 0 : map.getFileVersion();
            int newVersion = prevVersion + 1;
            if (newVersion > 255) newVersion = 1;

            String filename = id.toString() + "." + newVersion;

            // Сохранение файла
            Path uploadPath = Paths.get(uploadDir + "/" + (id % 10));
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Сохранение карты
            map.setFile(true);
            map.setFileVersion(newVersion);
            save(map);

            // Удаление файла прошлой версии
            if (prevVersion > 0) {
                String filenamePrev = id + "." + prevVersion;
                try {
                    Path filePathPrev = uploadPath.resolve(filenamePrev);
                    Files.delete(filePathPrev);
                }
                catch (NoSuchFileException ignored) {}
            }

            return "/uploads/" + (id%10) + "/" + filename;
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
