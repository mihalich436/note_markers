package com.easymarkersapp.easymarkersapp.repository;

import com.easymarkersapp.easymarkersapp.model.Map;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MapRepository extends JpaRepository<Map, Long> {
    List<Map> findByProjectId(Long projectId);
    @EntityGraph(attributePaths = {"project"})
    Optional<Map> findByIdAndProjectId(Long mapId, Long projectId);
}
