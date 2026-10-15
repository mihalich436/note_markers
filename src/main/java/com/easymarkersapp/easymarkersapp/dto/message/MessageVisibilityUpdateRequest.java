package com.easymarkersapp.easymarkersapp.dto.message;

import com.easymarkersapp.easymarkersapp.model.MarkerMessage;

public class MessageVisibilityUpdateRequest implements MessageUpdateRequest {
    private Long messageId;
    private Boolean visibility;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    @Override
    public void updateMessage(MarkerMessage message) {
        message.setVisibility(this.visibility);
    }
}
