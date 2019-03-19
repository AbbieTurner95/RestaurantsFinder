package com.example.abbieturner.restaurantsfinder.FirebaseAccess.Listeners;

import com.example.abbieturner.restaurantsfinder.FirebaseModels.RecommendedRestaurant;

import java.util.List;

public interface RecommendedRestaurantsListener {
    void OnAddRecommendedRestaurantCompleted(boolean hasFailed);
    void OnGetRecommendedRestaurantsCompleted(List<RecommendedRestaurant> restaurants, boolean hasFailed);
}
