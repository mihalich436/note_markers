package com.easymarkersapp.easymarkersapp.dto.note;

import com.easymarkersapp.easymarkersapp.model.Note;

import java.util.Map;

public class NoteWithRoleDto {
    private Note note;
    private String role;
    private Long userId;
    private java.util.Map<Long, String> userIdToNick;

    public NoteWithRoleDto() {
    }

    public NoteWithRoleDto(Note note, String role, Long userId, Map<Long, String> userIdToNick) {
        this.note = note;
        this.role = role;
        this.userId = userId;
        this.userIdToNick = userIdToNick;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Map<Long, String> getUserIdToNick() {
        return userIdToNick;
    }

    public void setUserIdToNick(Map<Long, String> userIdToNick) {
        this.userIdToNick = userIdToNick;
    }
}
