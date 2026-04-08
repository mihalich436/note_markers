package com.easymarkersapp.easymarkersapp.dto;

public class AccessUserDTO {
    private final Long id;
    private final String username;
    private final String email;
    private final String role;
    private final String roleDisplayName;
    private final boolean isOwner;

    public AccessUserDTO(Long id, String username, String email, String role, String roleDisplayName, boolean isOwner) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.roleDisplayName = roleDisplayName;
        this.isOwner = isOwner;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public String getRoleDisplayName() { return roleDisplayName; }
    public boolean isOwner() { return isOwner; }
}
