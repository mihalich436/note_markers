package com.easymarkersapp.easymarkersapp.dto;

public class ShareRequest {
    private String email;
    private String role; // READ_ONLY, CHAT, EDITOR, ADMIN
    private String nickname;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
