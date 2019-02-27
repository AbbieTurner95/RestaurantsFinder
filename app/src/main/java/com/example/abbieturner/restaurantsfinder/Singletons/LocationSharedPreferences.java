package com.example.abbieturner.restaurantsfinder.Singletons;

import com.example.abbieturner.restaurantsfinder.Data.UsersDefaultLocation;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class LocationSharedPreferences {
    private static LocationSharedPreferences theSingleton = null;
    private List<UsersDefaultLocation> usersLocation = new ArrayList<>();

    public static LocationSharedPreferences getInstance(){
        if (theSingleton == null) {
            theSingleton = new LocationSharedPreferences();
        }
        return theSingleton;
    }


    public List<UsersDefaultLocation> getLocations(){
        return usersLocation;
    }

    public void setLocations(List<UsersDefaultLocation> locations){
        usersLocation.addAll(locations);
    }

    public boolean userHasLocationsSet(String userId){
        for(int i = 0; i < usersLocation.size(); i++){
            if(usersLocation.get(i).getId().equals(userId)
                    && usersLocation.get(i).getLocation() != null){
                return true;
            }
        }

        return false;
    }

    public LatLng getUsersLocation(String userId){
        for(int i = 0; i < usersLocation.size(); i++){
            if(usersLocation.get(i).getId().equals(userId)
                    && usersLocation.get(i).getLocation() != null){
                return usersLocation.get(i).getLocation();
            }
        }

        return null;
    }
}
