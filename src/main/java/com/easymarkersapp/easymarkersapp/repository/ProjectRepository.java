package com.easymarkersapp.easymarkersapp.repository;

import com.easymarkersapp.easymarkersapp.model.Project;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByOwnerId(Long id);
    @EntityGraph(attributePaths = {"maps"})
    Optional<Project> findById(Long id);

    @EntityGraph(attributePaths = {"maps"})
    Optional<Project> findByIdAndOwnerId(Long id, Long ownerId);
}
