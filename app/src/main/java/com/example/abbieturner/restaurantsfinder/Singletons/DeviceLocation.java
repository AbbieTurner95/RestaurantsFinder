package com.example.abbieturner.restaurantsfinder.Singletons;

import com.google.android.gms.maps.model.LatLng;

public class DeviceLocation {
    private static DeviceLocation theSingleton = null;
    private LatLng location;
    private final float kmToMiles = 0.621471f;

    public static DeviceLocation getInstance() {
        if (theSingleton == null) {
            theSingleton = new DeviceLocation();
        }
        return theSingleton;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public boolean isLocationSet() {
        return location != null;
    }
}
