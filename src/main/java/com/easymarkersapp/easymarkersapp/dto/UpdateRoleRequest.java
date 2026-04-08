package com.easymarkersapp.easymarkersapp.dto;

public class UpdateRoleRequest {
    private String role;
    private String nickname;

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
