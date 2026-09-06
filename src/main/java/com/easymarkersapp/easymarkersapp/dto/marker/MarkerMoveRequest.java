package com.easymarkersapp.easymarkersapp.dto.marker;

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

    @Override
    public void updateMarker(Marker marker) {
        marker.setX(this.x);
        marker.setY(this.y);
    }
}
