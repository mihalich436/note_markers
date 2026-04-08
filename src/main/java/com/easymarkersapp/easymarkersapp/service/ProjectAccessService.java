package com.easymarkersapp.easymarkersapp.service;

import com.easymarkersapp.easymarkersapp.model.Project;
import com.easymarkersapp.easymarkersapp.model.ProjectAccess;
import com.easymarkersapp.easymarkersapp.model.User;
import com.easymarkersapp.easymarkersapp.repository.ProjectAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectAccessService {
    @Autowired
    private ProjectAccessRepository accessRepository;

    public List<ProjectAccess> findByProject(Project project) {
        return accessRepository.findByProject(project);
    }
    public List<ProjectAccess> findByUser(User user) {
        return accessRepository.findByUser(user);
    }

    public Optional<ProjectAccess> findByProjectAndUser(Project project, User user) {
        return accessRepository.findByProjectAndUser(project, user);
    }
    public Optional<ProjectAccess> findByProjectIdAndUser(Long projectId, User user) {
        return accessRepository.findByProjectIdAndUser(projectId, user);
    }

    public void deleteByProjectAndUser(Project project, User user) {
        accessRepository.deleteByProjectAndUser(project, user);
    }

    public ProjectAccess save(ProjectAccess access) {
        return accessRepository.save(access);
    }

    public boolean existsByProjectAndUser(Project project, User user) {
        return accessRepository.existsByProjectAndUser(project, user);
    }
}
