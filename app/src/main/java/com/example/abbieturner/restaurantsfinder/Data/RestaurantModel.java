package com.example.abbieturner.restaurantsfinder.Data;

import com.example.abbieturner.restaurantsfinder.FirebaseModels.Restaurant;

public class RestaurantModel {
    private boolean isFirebaseRestaurant;
    private Restaurant restaurantF;
    private com.example.abbieturner.restaurantsfinder.Data.Restaurant restaurantZ;

    public RestaurantModel(Restaurant restaurant){
        this.restaurantF = restaurant;
        this.isFirebaseRestaurant = true;
    }

    public RestaurantModel(com.example.abbieturner.restaurantsfinder.Data.Restaurant restaurant){
        this.restaurantZ = restaurant;
        this.isFirebaseRestaurant = false;
    }

    public Restaurant getFirebaseRestaurant(){
        return restaurantF;
    }

    public com.example.abbieturner.restaurantsfinder.Data.Restaurant getZomatoRestaurant(){
        return restaurantZ;
    }

    public boolean isFirebaseRestaurant(){
        return isFirebaseRestaurant;
    }
}
