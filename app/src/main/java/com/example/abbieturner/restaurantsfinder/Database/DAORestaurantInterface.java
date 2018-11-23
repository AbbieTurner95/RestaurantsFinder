package com.example.abbieturner.restaurantsfinder.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

import com.example.abbieturner.restaurantsfinder.Data.RestaurantsModel;

@Dao
public interface DAORestaurantInterface {

    @Insert
    void insertRestaurant(RestaurantsModel.Restaurant restaurant);

    @Delete
    void deleteResuarant (RestaurantsModel.Restaurant restaurant);
}