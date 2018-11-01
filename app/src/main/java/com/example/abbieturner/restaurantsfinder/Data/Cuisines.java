package com.example.abbieturner.restaurantsfinder.Data;

import java.util.List;

public class Cuisines {

    public List<Cuisine> cuisinesList;

    public Cuisines(List<Cuisine> cuisinesList) {
        this.cuisinesList = cuisinesList;
    }

    public List<Cuisine> getCuisinesList() {
        return cuisinesList;
    }

    public void setCuisinesList(List<Cuisine> cuisinesList) {
        this.cuisinesList = cuisinesList;
    }
}
