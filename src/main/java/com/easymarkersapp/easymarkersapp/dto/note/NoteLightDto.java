package com.easymarkersapp.easymarkersapp.dto.note;

import com.easymarkersapp.easymarkersapp.model.Note;

import java.time.LocalDateTime;

public class NoteLightDto {
    private Long id;
    private String title;
    private String description;
    private Boolean visibility;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static NoteLightDto convertToLightDto(Note note) {return new NoteLightDto(note); }

    public NoteLightDto(Note note) {
        this.id = note.getId();
        this.title = note.getTitle();
        this.description = note.getDescription();
        this.visibility = note.getVisibility();
        this.createdAt = note.getCreatedAt();
        this.updatedAt = note.getUpdatedAt();
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

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
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
