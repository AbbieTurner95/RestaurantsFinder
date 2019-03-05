package com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners;

import com.example.abbieturner.restaurantsfinder.FirebaseModels.Restaurant;

public interface RestaurantListener {
    void onRestaurantCreated(boolean hasFailed);

    void onRestaurantLoaded(Restaurant restaurant, boolean hasFailed);
}
