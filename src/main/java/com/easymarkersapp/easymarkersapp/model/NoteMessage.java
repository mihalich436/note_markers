package com.easymarkersapp.easymarkersapp.model;

import com.easymarkersapp.easymarkersapp.dto.message.MessageSaveRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "note_messages")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class NoteMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Note note;

    @Column(name = "user_id")
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(name = "visibility")
    private Boolean visibility;

    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    public NoteMessage() {
    }

    public NoteMessage(MessageSaveRequest request, Long noteId, Long userId) {
        this.text = request.getText();
        this.visibility = request.getVisibility();
        if (noteId == null) {
            this.note = null;
        }
        else {
            Note noteProxy = new Note();
            noteProxy.setId(noteId);
            this.note = noteProxy;
        }
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

    public Long getNoteId() {
        return note.getId();
    }

    public void setNoteId(Long noteId) {
        if (noteId == null) {
            this.note = null;
        }
        else {
            Note noteProxy = new Note();
            noteProxy.setId(noteId);
            this.note = noteProxy;
        }
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
}
