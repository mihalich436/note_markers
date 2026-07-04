package com.easymarkersapp.easymarkersapp.dto.map;

import com.easymarkersapp.easymarkersapp.model.Map;

public class MapWithRoleDTO {
    private Map map;
    private String role;
    private Long userId;
    private java.util.Map<Long, String> userIdToNick;

    public MapWithRoleDTO() {
    }

    public MapWithRoleDTO(Map map, String role, Long userId, java.util.Map<Long, String> userIdToNick) {
        this.map = map;
        this.role = role;
        this.userId = userId;
        this.userIdToNick = userIdToNick;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public java.util.Map<Long, String> getUserIdToNick() {
        return userIdToNick;
    }

    public void setUserIdToNick(java.util.Map<Long, String> userIdToNick) {
        this.userIdToNick = userIdToNick;
    }
}
