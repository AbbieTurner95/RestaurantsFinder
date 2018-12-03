package com.example.abbieturner.restaurantsfinder.Data;

import com.google.gson.annotations.SerializedName;

public class Cuisine {

    @SerializedName("cuisine_id")
    private int cuisine_id;
    @SerializedName("cuisine_name")
    private String cuisine_name;


    public int getCuisine_id() {
        return cuisine_id;
    }

    public void setCuisine_id(int cuisine_id) {
        this.cuisine_id = cuisine_id;
    }

    public String getCuisine_name() {
        return cuisine_name;
    }

    public void setCuisine_name(String cuisine_name) {
        this.cuisine_name = cuisine_name;
    }

    public Cuisine(int cuisine_id, String cuisine_name) {
        this.cuisine_id = cuisine_id;
        this.cuisine_name = cuisine_name;
    }

    @Override
    public String toString() {
        return cuisine_name;
    }
}
