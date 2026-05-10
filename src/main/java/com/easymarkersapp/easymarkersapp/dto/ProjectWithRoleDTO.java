package com.easymarkersapp.easymarkersapp.dto;

import com.easymarkersapp.easymarkersapp.model.Project;

public class ProjectWithRoleDTO {
    Project project;
    String role;

    public ProjectWithRoleDTO() {
    }

    public ProjectWithRoleDTO(Project project, String role) {
        this.project = project;
        this.role = role;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
