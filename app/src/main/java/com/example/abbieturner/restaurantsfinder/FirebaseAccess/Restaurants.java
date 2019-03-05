package com.example.abbieturner.restaurantsfinder.FirebaseAccess;

import android.support.annotation.NonNull;

import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners.RestaurantsListener;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.Restaurant;
import com.example.abbieturner.restaurantsfinder.Singletons.DeviceLocation;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Restaurants {

    private FirebaseDatabase database;
    private DatabaseReference restaurantsRef;
    private RestaurantsListener callback;
    private DatabaseReference geofireRef;
    private GeoFire geoFire;
    private List<String> restaurants;
    private DeviceLocation locationSingleton;
    private List<Restaurant> restaurantsList;

    public Restaurants(RestaurantsListener callback) {
        restaurants = new ArrayList<>();
        restaurantsList = new ArrayList<>();
        locationSingleton = DeviceLocation.getInstance();
        this.callback = callback;
        database = FirebaseDatabase.getInstance();
        restaurantsRef = database.getReference().child("restaurants");

        geofireRef = database.getReference().child("restaurantsGeoFire");
        geoFire = new GeoFire(geofireRef);
    }

    public void getRestaurants(final String cuisine) {
        restaurants.clear();
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(locationSingleton.getLocation().latitude, locationSingleton.getLocation().longitude), (200 * 1.609));

        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                restaurants.add(key);
            }

            @Override
            public void onKeyExited(String key) {
                String s = null;
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                String s = null;
            }

            @Override
            public void onGeoQueryReady() {
                getRestaurantsById(cuisine);
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                String s = null;
            }
        });
    }

    private void getRestaurantsById(final String cuisine) {
        if (restaurants.size() == 0) {
            callback.onRestaurantsLoaded(false, restaurantsList);
        }

        for (int i = 0; i < restaurants.size(); i++) {
            if (!isLastElement(i)) {
                restaurantsRef.child(restaurants.get(i)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Restaurant r = dataSnapshot.getValue(Restaurant.class);
                        if (r != null && r.getCuisine().equals(cuisine)) {
                            restaurantsList.add(r);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onRestaurantsLoaded(true, null);
                    }
                });
            } else {
                restaurantsRef.child(restaurants.get(i)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Restaurant r = dataSnapshot.getValue(Restaurant.class);
                        if (r != null && r.getCuisine().equals(cuisine)) {
                            r.setId(dataSnapshot.getKey());
                            restaurantsList.add(r);
                        }

                        callback.onRestaurantsLoaded(false, restaurantsList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onRestaurantsLoaded(true, null);
                    }
                });
            }

        }
    }

    private boolean isLastElement(int i) {
        return i == restaurants.size() - 1;
    }
}
