package com.easymarkersapp.easymarkersapp.dto.message;

import com.easymarkersapp.easymarkersapp.model.Message;

public class MessageTextUpdateRequest implements MessageUpdateRequest {
    private Long messageId;
    private Long markerId;
    private String text;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getMarkerId() {
        return markerId;
    }

    public void setMarkerId(Long markerId) {
        this.markerId = markerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void updateMessage(Message message) {
        message.setText(this.text);
    }
}
