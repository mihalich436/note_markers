package com.easymarkersapp.easymarkersapp.dto.note_message;

import com.easymarkersapp.easymarkersapp.model.NoteMessage;

public class NoteMessageTextUpdateRequest implements NoteMessageUpdateRequest {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void updateMessage(NoteMessage message) {
        message.setText(this.text);
    }
}
