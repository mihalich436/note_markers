package com.easymarkersapp.easymarkersapp.service;

import com.easymarkersapp.easymarkersapp.model.AccessRole;
import com.easymarkersapp.easymarkersapp.model.Project;
import com.easymarkersapp.easymarkersapp.model.ProjectAccess;
import com.easymarkersapp.easymarkersapp.model.User;
import com.easymarkersapp.easymarkersapp.repository.ProjectAccessRepository;
import com.easymarkersapp.easymarkersapp.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectAccessRepository accessRepository;

    public List<Project> findByOwnerId(Long ownerId) {
        return projectRepository.findByOwnerId(ownerId);
    }

//    @Transactional
    public List<Project> findByUser(User user) {
//        List<Project> projects = projectRepository.findByOwnerId(user.getId());
//        List<ProjectAccess> projectAccesses = accessRepository.findByUser(user);
//        projects.addAll(projectAccesses.stream().map(ProjectAccess::getProject).toList());
//        return projects;
        return accessRepository.findByUser(user).stream().map(ProjectAccess::getProject).toList();
    }

    @Transactional
    public Project findByProjectIdAndUser(Long projectId, User user) {
        Optional<Project> projectOptional = projectRepository.findByIdAndOwnerId(projectId, user.getId());
        if (projectOptional.isPresent()) {
            return projectOptional.get();
        }
        Optional<ProjectAccess> projectAccess = accessRepository.findByProjectIdAndUser(projectId, user);
        return projectAccess.map(ProjectAccess::getProject).orElse(null);
    }

    public Optional<Project> findById(Long id){
        return projectRepository.findById(id);
    }
    @Transactional
    public Project save(Project project) {
        Project createdProject = projectRepository.save(project);
        ProjectAccess projectAccess = new ProjectAccess(createdProject, new User(createdProject.getOwnerId()), AccessRole.ADMIN, "Владелец");
        accessRepository.save(projectAccess);
        return createdProject;
    }
    public boolean existsById(Long projectId) {
        return projectRepository.existsById(projectId);
    }
}
