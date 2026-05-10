package com.easymarkersapp.easymarkersapp.dto;

public class MapCreateRequest {
    private String title;
    private String description;
    private String imageUrl;
    private Boolean visibility;

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
        if (visibility == null) return true;
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }
}
