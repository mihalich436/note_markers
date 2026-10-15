package com.easymarkersapp.easymarkersapp.dto.message;

import com.easymarkersapp.easymarkersapp.model.MarkerMessage;

public class MessageTextUpdateRequest implements MessageUpdateRequest {
    private Long messageId;
    private String text;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void updateMessage(MarkerMessage message) {
        message.setText(this.text);
    }
}
