package com.example.abbieturner.restaurantsfinder.FirebaseAccess;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.abbieturner.restaurantsfinder.FirebaseModels.PopularRestaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PopularRestaurants {
    private FirebaseDatabase database;
    private DatabaseReference popularRestaurantsRef;
    private PopularRestaurantsListener callback;

    public PopularRestaurants(PopularRestaurantsListener callback){
        this.callback = callback;
        database = FirebaseDatabase.getInstance();
        popularRestaurantsRef = database.getReference().child("popularRestaurants");
    }

    public interface PopularRestaurantsListener{
        void onRestaurantsLoaded(List<PopularRestaurant> list, boolean hasFailed);
    }

    public void getPopularRestaurants(){
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

    public void removePopularRestaurant(final String restaurantId){
        popularRestaurantsRef.child(restaurantId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PopularRestaurant restaurant = dataSnapshot.getValue(PopularRestaurant.class);

                if(restaurant != null){
                    if(restaurant.hasMultipleLikes()){
                        restaurant.decreaseCountByOne();
                        updateRestaurant(restaurant);
                    }else{
                        removeRestaurantFromPopular(restaurantId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
                //callback.onBusinessLoadedCompleted(true, null);
            }
        });
    }

    public void upsertPopularRestaurant(final String restaurantId){
        popularRestaurantsRef.child(restaurantId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PopularRestaurant restaurant = dataSnapshot.getValue(PopularRestaurant.class);

                if(restaurant != null){
                    restaurant.increateCountByOne();
                    updateRestaurant(restaurant);
                }

                createRestaurant(restaurantId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
                //callback.onBusinessLoadedCompleted(true, null);
            }
        });
    }

    private void removeRestaurantFromPopular(String restaurantId){
        popularRestaurantsRef.child(restaurantId).removeValue();
    }

    private void updateRestaurant(PopularRestaurant restaurant){
        popularRestaurantsRef.child(restaurant.getRestaurantId()).child("count").setValue(restaurant.getCount());
    }

    private void createRestaurant(String restaurantId){
        popularRestaurantsRef.child(restaurantId).setValue()
                .child("count").setValue(1);
    }
}
