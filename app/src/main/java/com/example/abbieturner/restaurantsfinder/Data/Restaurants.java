package com.example.abbieturner.restaurantsfinder.Data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Restaurants {
    @SerializedName("restaurants")
    public List<Restaurant> restaurantsList;

    public Restaurants(List<Restaurant> restaurantsList) {
        this.restaurantsList = restaurantsList;
    }

    public List<Restaurant> getRestaurantsList() {
        return restaurantsList;
    }

    public void setRestaurantsList(List<Restaurant> restaurantsList) {
        this.restaurantsList = restaurantsList;
    }
}
