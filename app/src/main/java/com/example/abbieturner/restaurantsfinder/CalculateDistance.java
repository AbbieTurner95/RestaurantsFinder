package com.example.abbieturner.restaurantsfinder;

import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.Data.UsersLocation;

public class CalculateDistance {

    private static CalculateDistance theSingleton = null;

    private CalculateDistance() {
    }

    public static CalculateDistance getInstance() {
        if (theSingleton == null) {
            theSingleton = new CalculateDistance();
        }
        return theSingleton;
    }

    public String getRestaurantDistance(Restaurant restaurant) {
        return "Distance: " + Double.toString(calcDistance(restaurant)) + " miles";
    }

    private double calcDistance(Restaurant restaurant) {
        double distance = UsersLocation.getDistance(Double.parseDouble(restaurant.getLocation().getLatitude())
                , Double.parseDouble(restaurant.getLocation().getLongitude()));

        return Math.round(distance * 100.0) / 100.0;
    }
}
