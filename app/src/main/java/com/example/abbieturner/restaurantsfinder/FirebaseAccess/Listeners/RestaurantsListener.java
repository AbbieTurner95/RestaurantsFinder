package com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners;

import com.example.abbieturner.restaurantsfinder.FirebaseModels.Restaurant;

import java.util.List;

public interface RestaurantsListener {
    void onRestaurantsLoaded(boolean hasFailed, List<Restaurant> restaurants);
}
