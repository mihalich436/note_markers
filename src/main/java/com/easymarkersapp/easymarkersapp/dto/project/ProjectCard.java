package com.easymarkersapp.easymarkersapp.dto.project;

import com.easymarkersapp.easymarkersapp.model.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectCard {
    private Long id;
    private String title;
    private String description;
    private boolean isOwner;

    public ProjectCard(Project project, Long userId) {
        if (project != null) {
            this.id = project.getId();
            this.title = project.getTitle();
            this.description = project.getDescription();
            this.isOwner = userId.equals(project.getOwnerId());
        }
    }

    public static List<ProjectCard> create(List<Project> projects, Long userId) {
        List<ProjectCard> cards = new ArrayList<>(projects.size());
        for (Project project:projects) {
            cards.add(new ProjectCard(project, userId));
        }
        return cards;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isOwner() {
        return isOwner;
    }
}
