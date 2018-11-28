package com.example.abbieturner.restaurantsfinder.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.abbieturner.restaurantsfinder.Data.Restaurant;
import com.example.abbieturner.restaurantsfinder.DatabaseModels.DatabaseRestaurant;

import java.util.List;

import retrofit2.http.GET;

@Dao
public interface DAORestaurantInterface {

    @Insert
    void insertRestaurant(DatabaseRestaurant restaurant);

    @Query("SELECT * FROM restaurantsDatabase")
    public List<DatabaseRestaurant> getRestaurants();

    @Delete
    void deleteResuarant (DatabaseRestaurant restaurant);
}