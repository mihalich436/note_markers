package com.easymarkersapp.easymarkersapp.model;

import com.easymarkersapp.easymarkersapp.model.Project;
import com.easymarkersapp.easymarkersapp.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_access",
        uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "user_id"}))
public class ProjectAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccessRole role;

    @Column(name = "granted_at")
    private LocalDateTime grantedAt;

    @Column(name = "nickname")
    private String nickname;

    @PrePersist
    protected void onCreate() {
        grantedAt = LocalDateTime.now();
    }

    // Конструкторы
    public ProjectAccess() {}

    public ProjectAccess(Project project, User user, AccessRole role, String nickname) {
        this.project = project;
        this.user = user;
        this.role = role;
        this.nickname = nickname;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public AccessRole getRole() { return role; }
    public void setRole(AccessRole role) { this.role = role; }

    public LocalDateTime getGrantedAt() { return grantedAt; }
    public void setGrantedAt(LocalDateTime grantedAt) { this.grantedAt = grantedAt; }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
