package com.easymarkersapp.easymarkersapp.dto;

import com.easymarkersapp.easymarkersapp.model.Map;

public class MapWithRoleDTO {
    private Map map;
    private String role;
    //> add user's nicknames


    public MapWithRoleDTO() {
    }

    public MapWithRoleDTO(Map map, String role) {
        this.map = map;
        this.role = role;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
