package com.easymarkersapp.easymarkersapp.model;

public enum AccessRole {
    READ_ONLY("Только чтение"),
    CHAT("Чат"),
    EDITOR("Редактирование"),
    ADMIN("Мастер");

    private final String displayName;

    AccessRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
