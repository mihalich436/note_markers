package com.easymarkersapp.easymarkersapp.dto;

import com.easymarkersapp.easymarkersapp.model.Marker;

public class MarkerSaveRequest implements MarkerRequest {
    private String title;
    private String note;
    private String description;

    private Double x;
    private Double y;
    private String color;
    private String shape;
    private Integer size;

    private Boolean visibility;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(Boolean visibility) {
        this.visibility = visibility;
    }

    @Override
    public void updateMarker(Marker marker) {
        marker.setTitle(this.title);
        marker.setNote(this.note);
        marker.setDescription(this.description);
        marker.setX(this.x);
        marker.setY(this.y);
        marker.setColor(this.color);
        marker.setShape(this.shape);
        marker.setSize(this.size);
        marker.setVisibility(this.visibility);
    }
}
