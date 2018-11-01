package com.example.abbieturner.restaurantsfinder.Data;

public class Cuisine {

    private int cuisine_id;
    private String cuisine_name;
    // new comment


    public int getCuisine_id(){
        return cuisine_id;
    }

    public void setCuisine_id(int cuisine_id){
        this.cuisine_id = cuisine_id;
    }

    public String getCuisine_name(){
        return  cuisine_name;
    }

    public void setCuisine_name(String cuisine_name){
        this.cuisine_name = cuisine_name;
    }

    public Cuisine(int cuisine_id, String cuisine_name){
        this.cuisine_id = cuisine_id;
        this.cuisine_name = cuisine_name;
    }
}
