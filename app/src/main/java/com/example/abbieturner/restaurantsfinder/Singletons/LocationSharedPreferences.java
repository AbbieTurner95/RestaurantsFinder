package com.example.abbieturner.restaurantsfinder.Singletons;

import com.example.abbieturner.restaurantsfinder.Data.UsersDefaultLocation;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class LocationSharedPreferences {
    private static LocationSharedPreferences theSingleton = null;
    private LatLng usersLocation = null;

    public static LocationSharedPreferences getInstance(){
        if (theSingleton == null) {
            theSingleton = new LocationSharedPreferences();
        }
        return theSingleton;
    }


    public LatLng getLocation(){
        return usersLocation;
    }

    public void setLocation(LatLng location){
        usersLocation = location;
    }

    public boolean userHasLocationsSet(){
        return usersLocation != null;
    }

    public LatLng getUsersLocation(){
        return usersLocation;
    }
}
