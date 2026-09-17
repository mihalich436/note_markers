package com.easymarkersapp.easymarkersapp.repository;

import com.easymarkersapp.easymarkersapp.model.ProjectShareLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectShareLinkRepository extends JpaRepository<ProjectShareLink, Long> {
    Optional<ProjectShareLink> findByToken(String token);
    Optional<ProjectShareLink> findByProjectId(Long projectId);
    void deleteAllByProjectId(Long projectId);
}
