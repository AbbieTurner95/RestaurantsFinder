package com.example.abbieturner.restaurantsfinder.FirebaseAccess;

import android.support.annotation.NonNull;

import com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners.RecommendedRestaurantsListener;
import com.example.abbieturner.restaurantsfinder.FirebaseModels.RecommendedRestaurant;
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

public class RecommendedRestaurants {
    private FirebaseDatabase database;
    private DatabaseReference recommendedRef;
    private RecommendedRestaurantsListener callback;

    public RecommendedRestaurants(RecommendedRestaurantsListener callback) {
        this.callback = callback;
        database = FirebaseDatabase.getInstance();
        recommendedRef = database.getReference().child("recommended");
    }

    public void getRecommendedRestaurants(String userId) {
        final DatabaseReference ref = recommendedRef.child(userId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<RecommendedRestaurant> list = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    RecommendedRestaurant restaurant = postSnapshot.getValue(RecommendedRestaurant.class);
                    list.add(restaurant);
                }

                ref.removeEventListener(this);
                callback.OnGetRecommendedRestaurantsCompleted(list, false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                ref.removeEventListener(this);
                callback.OnGetRecommendedRestaurantsCompleted(null, true);
            }
        });
    }

    public void addRecommendedRestaurant(String userId, String restaurantId, String restaurantName, String pictureUrl, String token) {
        recommendedRef
                .child(userId)
                .child(restaurantId)
                .setValue(createRestaurantHashMap(restaurantId, restaurantName, pictureUrl, token))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.OnAddRecommendedRestaurantCompleted(false);
                        } else {
                            callback.OnAddRecommendedRestaurantCompleted(true);
                        }
                    }
                });
    }

    private HashMap createRestaurantHashMap(String restaurantId, String restaurantName, String pictureUrl, String token) {
        HashMap hm = new HashMap();

        hm.put("restaurantId", restaurantId);
        hm.put("restaurantName", restaurantName);
        hm.put("pictureUrl", pictureUrl);
        hm.put("token", token);
        return hm;
    }
}