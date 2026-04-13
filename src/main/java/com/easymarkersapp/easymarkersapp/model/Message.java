package com.easymarkersapp.easymarkersapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "marker_messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "marker_id")
//    @JsonBackReference
    private Long markerId;

    @Column(name = "user_id")
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(name = "visibility")
    private Boolean visibility;

    @Column(name = "created_at")
    private String createdAt;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "markerId", nullable = false, insertable = false, updatable = false)
//    @JsonIgnore
//    private Marker marker;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMarkerId() {
        return markerId;
    }

    public void setMarkerId(Long markerId) {
        this.markerId = markerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

//    public Marker getMarker() {
//        return marker;
//    }
//
//    public void setMarker(Marker marker) {
//        this.marker = marker;
//    }
}
