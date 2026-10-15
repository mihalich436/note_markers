package com.easymarkersapp.easymarkersapp.dto.project;

import com.easymarkersapp.easymarkersapp.model.Project;

public class ProjectWithRoleDto {
    ProjectWithMapsLightDto project;
    String role;

    public ProjectWithRoleDto() {
    }

    public ProjectWithRoleDto(Project project, String role) {
        this.project = new ProjectWithMapsLightDto(project);
        this.role = role;
    }

    public ProjectWithRoleDto(ProjectWithMapsLightDto project, String role) {
        this.project = project;
        this.role = role;
    }

    public ProjectWithMapsLightDto getProject() {
        return project;
    }

    public void setProject(ProjectWithMapsLightDto project) {
        this.project = project;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
