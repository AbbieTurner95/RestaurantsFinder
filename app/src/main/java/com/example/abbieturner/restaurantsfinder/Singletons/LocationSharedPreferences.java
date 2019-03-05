package com.example.abbieturner.restaurantsfinder.Singletons;

import com.google.android.gms.maps.model.LatLng;

public class LocationSharedPreferences {
    private static LocationSharedPreferences theSingleton = null;
    private LatLng usersLocation = null;

    public static LocationSharedPreferences getInstance() {
        if (theSingleton == null) {
            theSingleton = new LocationSharedPreferences();
        }
        return theSingleton;
    }


    public LatLng getLocation() {
        return usersLocation;
    }

    public void setLocation(LatLng location) {
        usersLocation = location;
    }

    public boolean userHasLocationsSet() {
        return usersLocation != null;
    }

    public LatLng getUsersLocation() {
        return usersLocation;
    }
}
