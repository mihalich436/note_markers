package com.easymarkersapp.easymarkersapp.dto.message;

public class MessageSaveRequest {
    private String text;
    private Boolean visibility;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }
}
