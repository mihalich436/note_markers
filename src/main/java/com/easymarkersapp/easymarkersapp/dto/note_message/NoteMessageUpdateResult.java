package com.easymarkersapp.easymarkersapp.dto.note_message;

import com.easymarkersapp.easymarkersapp.model.NoteMessage;

public record NoteMessageUpdateResult(Long noteId, NoteMessage message) {
}
