package com.easymarkersapp.easymarkersapp.dto.map;

import com.easymarkersapp.easymarkersapp.model.Map;

import java.time.LocalDateTime;

public class MapLightDto {
    private Long id;
    private String title;
    private String description;
    private String imageUrl;
    private Boolean visibility;
    private Boolean isFile;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MapLightDto convertToLightDto(Map map) {
        return new MapLightDto(map);
    }

    public MapLightDto(Map map) {
        this.id = map.getId();
        this.title = map.getTitle();
        this.description = map.getDescription();
        this.imageUrl = map.getImageUrl();
        this.visibility = map.getVisibility();
        this.isFile = map.getFile();
        this.createdAt = map.getCreatedAt();
        this.updatedAt = map.getUpdatedAt();
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public Boolean getFile() {
        return isFile;
    }

    public void setFile(Boolean file) {
        isFile = file;
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
}
