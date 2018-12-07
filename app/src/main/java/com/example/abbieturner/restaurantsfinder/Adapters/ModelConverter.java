package com.example.abbieturner.restaurantsfinder.Adapters;

import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.DatabaseModels.DatabaseRestaurant;

import java.util.ArrayList;
import java.util.List;

public class ModelConverter {
    private static ModelConverter theSingleton = null;


    public static ModelConverter getInstance() {
        if (theSingleton == null) {
            theSingleton = new ModelConverter();
        }
        return theSingleton;    // here’s one I made earlier !!
    }

    public DatabaseRestaurant convertToDatabaseRestaurant(Restaurant restaurant) {
        return new DatabaseRestaurant(restaurant);
    }

    private Restaurant convertToRestaurant(DatabaseRestaurant databaseRestaurant) {
        return new Restaurant(databaseRestaurant);
    }

    public List<Restaurant> convertToRestaurants(List<DatabaseRestaurant> databaseRestaurants) {

        List<Restaurant> convertedRestaurants = new ArrayList<>();

        for (final DatabaseRestaurant restaurant : databaseRestaurants) {
            convertedRestaurants.add(convertToRestaurant(restaurant));
        }

        return convertedRestaurants;
    }
}


