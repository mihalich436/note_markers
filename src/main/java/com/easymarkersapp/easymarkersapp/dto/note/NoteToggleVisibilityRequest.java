package com.easymarkersapp.easymarkersapp.dto.note;

import com.easymarkersapp.easymarkersapp.model.Note;

public class NoteToggleVisibilityRequest implements NoteUpdateRequest {
    private Boolean visibility;

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    @Override
    public void updateNote(Note note) {
        note.setVisibility(this.visibility);
    }
}
