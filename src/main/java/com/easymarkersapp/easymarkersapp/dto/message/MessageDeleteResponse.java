package com.easymarkersapp.easymarkersapp.dto.message;

public class MessageDeleteResponse {
    private Long messageId;
    private Long parentId;

    public MessageDeleteResponse(Long messageId, Long parentId) {
        this.messageId = messageId;
        this.parentId = parentId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
