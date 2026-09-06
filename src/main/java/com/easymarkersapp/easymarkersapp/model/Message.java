package com.easymarkersapp.easymarkersapp.model;

import com.easymarkersapp.easymarkersapp.dto.message.MessageSaveRequest;
import jakarta.persistence.*;

import java.time.LocalDateTime;

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
    private LocalDateTime createdAt;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "markerId", nullable = false, insertable = false, updatable = false)
//    @JsonIgnore
//    private Marker marker;


    public Message() {
    }

    public Message(MessageSaveRequest request, Long userId) {
        this.text = request.getText();
        this.visibility = request.getVisibility();
        this.markerId = request.getMarkerId();
        this.userId = userId;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Message copyWithoutId(Long markerId) {
        Message copy = new Message();
        copy.setText(this.text);
        copy.setVisibility(this.visibility);
        copy.setUserId(this.userId);
        copy.setMarkerId(markerId);
        return copy;
    }
}
