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

    public String getRestaurantDistance(Double lat, Double lng) {
        return "Distance: " + Double.toString(calcDistance(lat, lng)) + " miles";
    }

    public double calcDistance(Double lat, Double lng) {
        double distance = UsersLocation.getDistance(lat, lng);

        return Math.round(distance * 100.0) / 100.0;
    }
}
