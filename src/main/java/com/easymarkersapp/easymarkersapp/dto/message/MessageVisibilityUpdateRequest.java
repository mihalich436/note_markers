package com.easymarkersapp.easymarkersapp.dto.message;

import com.easymarkersapp.easymarkersapp.model.Message;

public class MessageVisibilityUpdateRequest implements MessageUpdateRequest {
    private Long messageId;
    private Long markerId;
    private Boolean visibility;

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

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    @Override
    public void updateMessage(Message message) {
        message.setVisibility(this.visibility);
    }
}
