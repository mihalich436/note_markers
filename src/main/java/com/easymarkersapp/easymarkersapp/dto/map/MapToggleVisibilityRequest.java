package com.easymarkersapp.easymarkersapp.dto.map;

import com.easymarkersapp.easymarkersapp.model.Map;

public class MapToggleVisibilityRequest implements MapRequest {
    private Boolean visibility;

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    @Override
    public boolean updateMap(Map map) {
        map.setVisibility(this.visibility);
        return false;
    }
}
