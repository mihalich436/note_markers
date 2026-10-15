package com.easymarkersapp.easymarkersapp.service;

import com.easymarkersapp.easymarkersapp.dto.map.MapWithRoleDTO;
import com.easymarkersapp.easymarkersapp.exception.NotImageUploadedException;
import com.easymarkersapp.easymarkersapp.model.*;
import com.easymarkersapp.easymarkersapp.repository.MapRepository;
import com.easymarkersapp.easymarkersapp.repository.ProjectAccessRepository;
import com.easymarkersapp.easymarkersapp.util.ImageTypeDetector;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MapService {
    @Autowired
    private MapRepository mapRepository;
    @Autowired
    private ProjectAccessRepository accessRepository;
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Transactional
    public MapWithRoleDTO findByIdAndCheckAccess(Long id, User user) {
        Optional<Map> mapOptional = mapRepository.findWithMarkersById(id);
        if (mapOptional.isPresent()) {
            Map map = mapOptional.get();
            Project project = map.getProject();
            List<ProjectAccess> projectAccessList = accessRepository.findByProject(project);
            Optional<ProjectAccess> projectAccessOptional = projectAccessList.stream()
                    .filter(projectAccess -> user.getId().equals(projectAccess.getUser().getId()))
                    .findFirst();
            if (projectAccessOptional.isPresent()) {
                ProjectAccess projectAccess = projectAccessOptional.get();
                if (projectAccess.getRole() != AccessRole.ADMIN) {
                    if (!map.getVisibility()) return null;
                    map.setMarkers(map.getMarkers().stream().filter(Marker::getVisibility).toList());
                }
                java.util.Map<Long, String> userIdToNick = projectAccessList.stream()
                        .collect(Collectors.toMap(
                                access -> access.getUser().getId(),
                                ProjectAccess::getNickname
                        ));
                return new MapWithRoleDTO(map, projectAccess.getRole().name(), user.getId(), userIdToNick);
            }
        }
        return null;
    }
    @Transactional
    public MapWithRoleDTO findByIdAndCheckShareAccess(Long id, Long projectId) {
        Optional<Map> mapOptional = mapRepository.findByIdAndProjectId(id, projectId);
        if (mapOptional.isPresent()) {
            Map map = mapOptional.get();
            Project project = map.getProject();
            List<ProjectAccess> projectAccessList = accessRepository.findByProject(project);

            if (!map.getVisibility()) return null;
            map.setMarkers(map.getMarkers().stream().filter(Marker::getVisibility).toList());
            java.util.Map<Long, String> userIdToNick = projectAccessList.stream()
                    .collect(Collectors.toMap(
                            access -> access.getUser().getId(),
                            ProjectAccess::getNickname
                    ));
            return new MapWithRoleDTO(map, AccessRole.READ_ONLY.name(), null, userIdToNick);
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
        if (map == null) return;
        String fileName = map.getFileName();
        if (fileName == null || fileName.isBlank()) return;

        Path uploadPath = Paths.get(uploadDir, String.valueOf(map.getId() % 10));
        Path filePath = uploadPath.resolve(fileName).normalize();
        if (filePath.startsWith(uploadPath)) {
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException ignored) {}
        }

        map.setFileName(null);
    }
    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024;

    @Transactional
    public String saveImage(Long id, User user, MultipartFile file)
            throws NotImageUploadedException, IOException {

        Map map = findByIdAndCheckRole(id, user, AccessRole.ADMIN);
        if (map == null) {
            return null;
        }

        if (file.isEmpty()) {
            throw new NotImageUploadedException("Файл пустой");
        }
        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new NotImageUploadedException("Файл слишком большой (макс. 10 МБ)");
        }

        String extension;
        try (InputStream in = new BufferedInputStream(file.getInputStream())) {
            extension = ImageTypeDetector.detectExtension(in)
                    .orElseThrow(() -> new NotImageUploadedException("Неподдерживаемый формат изображения"));
        }

        String newFileName = UUID.randomUUID() + "." + extension;

        Path uploadPath = Paths.get(uploadDir, String.valueOf(id % 10));
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(newFileName).normalize();
        if (!filePath.startsWith(uploadPath)) {
            throw new IOException("Некорректный путь файла");
        }

        // 1. Записать новый файл
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            // подчистить возможный частичный файл
            try { Files.deleteIfExists(filePath); } catch (IOException ignored) {}
            throw e;
        }

        // 2. Обновить БД
        String oldFileName = map.getFileName();
        map.setFile(true);
        map.setFileName(newFileName);
        save(map);

        // 3. Удалить старый файл (если был) — уже после успешного сохранения
        if (oldFileName != null && !oldFileName.isBlank() && !oldFileName.equals(newFileName)) {
            Path oldPath = uploadPath.resolve(oldFileName).normalize();
            if (oldPath.startsWith(uploadPath)) {
                try {
                    Files.deleteIfExists(oldPath);
                } catch (IOException ignored) {}
            }
        }

        return "/uploads/" + (id % 10) + "/" + newFileName;
    }
    @Transactional
    public boolean existsByIdAndUser(Long id, User user) {
        Optional<Map> mapOptional = mapRepository.findById(id);
        if (mapOptional.isPresent()) {
            Map map = mapOptional.get();
            Project project = new Project(map.getProjectId());
            return accessRepository.existsByProjectAndUser(project, user);
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

    public Map save(Map map) {
        return mapRepository.save(map);
    }

    public void delete(Map map) {
        mapRepository.delete(map);
    }
}
