package com.easymarkersapp.easymarkersapp.service;

import com.easymarkersapp.easymarkersapp.model.Project;
import com.easymarkersapp.easymarkersapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> findByOwnerId(Long ownerId) {
        return projectRepository.findByOwnerId(ownerId);
    }

    public Optional<Project> findById(Long id){
        return projectRepository.findById(id);
    }
    public Project save(Project project) {
        return projectRepository.save(project);
    }
    public boolean existsById(Long projectId) {
        return projectRepository.existsById(projectId);
    }
}
