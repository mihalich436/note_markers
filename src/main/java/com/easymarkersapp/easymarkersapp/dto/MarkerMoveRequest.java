package com.easymarkersapp.easymarkersapp.dto;

import com.easymarkersapp.easymarkersapp.model.Marker;

public class MarkerMoveRequest implements MarkerRequest {
    private Double x;
    private Double y;

    public MarkerMoveRequest() {
    }

    public MarkerMoveRequest(Double x, Double y) {
        this.x = x;
        this.y = y;
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

    @Override
    public void updateMarker(Marker marker) {
        marker.setX(this.x);
        marker.setY(this.y);
    }
}
