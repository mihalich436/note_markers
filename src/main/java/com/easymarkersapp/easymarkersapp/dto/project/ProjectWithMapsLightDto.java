package com.easymarkersapp.easymarkersapp.dto.project;

import com.easymarkersapp.easymarkersapp.dto.map.MapLightDto;
import com.easymarkersapp.easymarkersapp.model.Project;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectWithMapsLightDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<MapLightDto> maps;

    public ProjectWithMapsLightDto(Project project) {
        this.id = project.getId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.createdAt = project.getCreatedAt();
        this.updatedAt = project.getUpdatedAt();
        this.maps = project.getMaps().stream().map(MapLightDto::convertToLightDto).toList();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<MapLightDto> getMaps() {
        return maps;
    }

    public void setMaps(List<MapLightDto> maps) {
        this.maps = maps;
    }
}
