package com.easymarkersapp.easymarkersapp.dto.note_message;

import com.easymarkersapp.easymarkersapp.model.NoteMessage;

public class NoteMessageVisibilityUpdateRequest implements NoteMessageUpdateRequest {
    private Boolean visibility;

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    @Override
    public void updateMessage(NoteMessage message) {
        message.setVisibility(this.visibility);
    }
}
