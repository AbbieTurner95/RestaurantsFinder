package com.example.abbieturner.restaurantsfinder.Data;

import java.util.ArrayList;
import java.util.List;

public class CuisinesSingleton {
    private static CuisinesSingleton theSingleton = null;
    private List<Cuisine> cuisines;

    private CuisinesSingleton() {
        cuisines = new ArrayList<>();
    }

    public static CuisinesSingleton getInstance() {
        if (theSingleton == null) {
            theSingleton = new CuisinesSingleton();
        }
        return theSingleton;
    }

    public List<Cuisine> getCuisines() {
        return this.cuisines;
    }

    public void setCuisines(List<Cuisine> cuisines) {
        this.cuisines = cuisines;
    }


}
