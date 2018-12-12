package com.example.abbieturner.restaurantsfinder.Data;

public class FilterModel {

    private static FilterModel theSingleton = null;

    private FilterModel() {
    }

    public static FilterModel getInstance() {
        if (theSingleton == null) {
            theSingleton = new FilterModel();
        }
        return theSingleton;
    }

    private String search;
    private int distance;

    public FilterModel(String search, int distance){
        this.search = search;
        this.distance = distance;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }


}
