package com.easymarkersapp.easymarkersapp.dto.message;

public class MessageDeleteResponse {
    private Long messageId;
    private Long markerId;

    public MessageDeleteResponse(Long messageId, Long markerId) {
        this.messageId = messageId;
        this.markerId = markerId;
    }

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
}
