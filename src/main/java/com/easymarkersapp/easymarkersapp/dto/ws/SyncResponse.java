package com.easymarkersapp.easymarkersapp.dto.ws;

public class SyncResponse <T> {
    private String entityType;
    private String action;
    private T object;

    public SyncResponse() {
    }

    public SyncResponse(String entityType, String action, T object) {
        //> set entity type automatically from class simple name?
        this.entityType = entityType;
        this.action = action;
        this.object = object;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
