package com.example.abbieturner.restaurantsfinder.Data;

import com.google.android.gms.maps.model.LatLng;

public class UsersDefaultLocation {
    private String id;
    private LatLng location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
