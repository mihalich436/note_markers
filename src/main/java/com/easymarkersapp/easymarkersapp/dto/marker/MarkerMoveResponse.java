package com.easymarkersapp.easymarkersapp.dto.marker;

import com.easymarkersapp.easymarkersapp.model.Marker;

public class MarkerMoveResponse {
    private Long id;
    private Double x;
    private Double y;

    public MarkerMoveResponse() {
    }

    public MarkerMoveResponse(Marker marker) {
        this.id = marker.getId();
        this.x = marker.getX();
        this.y = marker.getY();
    }

    public MarkerMoveResponse(Long id, Double x, Double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
