package com.easymarkersapp.easymarkersapp.model;

import com.easymarkersapp.easymarkersapp.dto.marker.MarkerSaveRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "markers")
public class Marker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;
    private Double x;
    private Double y;
    private String title;
    private String note;

    @Column(columnDefinition = "TEXT")
    private String description;
    private String color;
    private String shape;
    private Integer size;

    private Boolean visibility;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private Long mapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapId", nullable = false, insertable = false, updatable = false)
    @JsonIgnore
    private Map map;

    // Связь с проектом (много заметок -> один проект)
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "markerId")
//    @JsonManagedReference
    private List<Message> messages = new ArrayList<>();

    public Marker(MarkerSaveRequest createRequest) {
        this.number = createRequest.getNumber();
        this.title = createRequest.getTitle();
        this.note = createRequest.getNote();
        this.description = createRequest.getDescription();
        this.x = createRequest.getX();
        this.y = createRequest.getY();
        this.color = createRequest.getColor();
        this.shape = createRequest.getShape();
        this.size = createRequest.getSize();
        this.visibility = createRequest.getVisibility();
    }

    public Marker() {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Long getMapId() {
        return mapId;
    }

    public void setMapId(Long mapId) {
        this.mapId = mapId;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Marker copy() {
        Marker copy = new Marker();
        copy.setNumber(this.number);
        copy.setX(this.x);
        copy.setY(this.y);
        copy.setTitle(this.title);
        copy.setNote(this.note);
        copy.setDescription(this.description);
        copy.setColor(this.color);
        copy.setShape(this.shape);
        copy.setSize(this.size);
        copy.setVisibility(this.visibility);
        copy.setMapId(this.mapId);
        return copy;
    }
}
