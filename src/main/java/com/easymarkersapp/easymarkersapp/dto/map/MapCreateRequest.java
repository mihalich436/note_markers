package com.easymarkersapp.easymarkersapp.dto.map;

import com.easymarkersapp.easymarkersapp.model.Map;

public class MapCreateRequest implements MapRequest {
    private String title;
    private String description;
    private String imageUrl;
    private Boolean visibility;
    private Boolean isFile;

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

    public Boolean getFile() {
        return isFile;
    }

    public void setFile(Boolean file) {
        isFile = file;
    }

    public boolean updateMap(Map map) {
        map.setTitle(this.getTitle());
        map.setDescription(this.getDescription());
        map.setImageUrl(this.getImageUrl());
        map.setVisibility(this.getVisibility());
        boolean isFilePrev = map.getFile();
        map.setFile(this.getFile());
        return (!this.getFile() && isFilePrev);
    }
}
