package com.example.abbieturner.restaurantsfinder.FirebaseAccess;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.abbieturner.restaurantsfinder.FirebaseModels.PopularRestaurant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PopularRestaurants {
    private FirebaseDatabase database;
    private DatabaseReference popularRestaurantsRef;
    private PopularRestaurantsListener callback;

    public PopularRestaurants(PopularRestaurantsListener callback) {
        this.callback = callback;
        database = FirebaseDatabase.getInstance();
        popularRestaurantsRef = database.getReference().child("popularRestaurants");
    }

    public interface PopularRestaurantsListener {
        void onRestaurantsLoaded(List<PopularRestaurant> list, boolean hasFailed);

        void onRestaurantUpdated();
    }

    public void getPopularRestaurants() {
        popularRestaurantsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<PopularRestaurant> list = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    PopularRestaurant restaurant = postSnapshot.getValue(PopularRestaurant.class);
                    restaurant.setRestaurantId(postSnapshot.getKey());
                    list.add(restaurant);
                }
                popularRestaurantsRef.removeEventListener(this);
                callback.onRestaurantsLoaded(list, false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onRestaurantsLoaded(null, true);
            }
        });
    }

    public void removePopularRestaurant(final String restaurantId) {
        final DatabaseReference dr = popularRestaurantsRef.child(restaurantId);
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PopularRestaurant restaurant = dataSnapshot.getValue(PopularRestaurant.class);

                if (restaurant != null) {
                    if (restaurant.hasMultipleLikes()) {
                        restaurant.decreaseCountByOne();
                        updateRestaurant(restaurant);
                    } else {
                        removeRestaurantFromPopular(restaurantId);
                    }
                }

                dr.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });
    }

    public void upsertPopularRestaurant(final String restaurantId, final String restaurantName) {
        final DatabaseReference df = popularRestaurantsRef.child(restaurantId);
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PopularRestaurant restaurant = dataSnapshot.getValue(PopularRestaurant.class);

                if (restaurant != null) {
                    restaurant.increateCountByOne();
                    updateRestaurant(restaurant);
                } else {
                    createRestaurant(restaurantId, restaurantName);
                }

                df.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onRestaurantUpdated();
                popularRestaurantsRef.removeEventListener(this);
            }
        });
    }

    private void createRestaurant(String restaurantId, String restaurantName) {
        popularRestaurantsRef
                .child(restaurantId)
                .setValue(createHashMap(restaurantId, restaurantName))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onRestaurantUpdated();
                        } else {
                            callback.onRestaurantUpdated();
                        }
                    }
                });
    }

    private void removeRestaurantFromPopular(String restaurantId) {
        popularRestaurantsRef
                .child(restaurantId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        callback.onRestaurantUpdated();
                    }
                });
    }

    private void updateRestaurant(PopularRestaurant restaurant) {
        if (restaurant != null) {
            popularRestaurantsRef
                    .child(restaurant.getRestaurantId())
                    .child("count")
                    .setValue(restaurant.getCount())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                callback.onRestaurantUpdated();
                            } else {
                                callback.onRestaurantUpdated();
                            }
                        }
                    });
        }
    }

    private HashMap createHashMap(String id, String name) {
        HashMap hm = new HashMap();

        hm.put("restaurantId", id);
        hm.put("count", 1);
        hm.put("name", name);

        return hm;
    }
}
