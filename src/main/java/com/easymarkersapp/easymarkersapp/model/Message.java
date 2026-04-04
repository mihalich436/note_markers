package com.easymarkersapp.easymarkersapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "marker_messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "marker_id")
    private Long markerId;

    @Column(name = "user_id")
    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(name = "created_at")
    private String createdAt;
}
