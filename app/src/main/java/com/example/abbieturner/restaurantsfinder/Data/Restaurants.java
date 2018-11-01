package com.example.abbieturner.restaurantsfinder.Data;

import java.util.List;

public class Restaurants {

    public List<Restaurant> restaurantList;

    public List<Restaurant> getRestaurantList() {
        return restaurantList;
    }

    public void setRestaurantList(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    public Restaurants(List<Restaurant> restaurantList) {
        this.restaurantList = getRestaurantList();
    }
}
