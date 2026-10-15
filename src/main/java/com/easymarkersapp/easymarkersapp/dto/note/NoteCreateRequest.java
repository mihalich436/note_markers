package com.easymarkersapp.easymarkersapp.dto.note;

import com.easymarkersapp.easymarkersapp.model.Map;
import com.easymarkersapp.easymarkersapp.model.Note;

public class NoteCreateRequest implements NoteUpdateRequest {
    private String title;
    private String description;
    private Boolean visibility;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    public void updateNote(Note note) {
        note.setTitle(this.getTitle());
        note.setDescription(this.getDescription());
        note.setVisibility(this.getVisibility());
    }
}
