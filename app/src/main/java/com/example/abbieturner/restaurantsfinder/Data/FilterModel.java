package com.example.abbieturner.restaurantsfinder.Data;

public class FilterModel {

    private static FilterModel theSingleton = null;

    private FilterModel() {
        delivery = false;
        stepFreeAccess = false;
        accessibleToilets = false;
        vegan = false;
        vegetarian = false;
        glutenFree = false;
        dairyFree = false;
    }

    public static FilterModel getInstance() {
        if (theSingleton == null) {
            theSingleton = new FilterModel();
        }
        return theSingleton;
    }

    private String search;
    private int distance, rating;
    private boolean delivery, stepFreeAccess, accessibleToilets, vegan, vegetarian, glutenFree, dairyFree;


    public static FilterModel getTheSingleton() {
        return theSingleton;
    }

    public static void setTheSingleton(FilterModel theSingleton) {
        FilterModel.theSingleton = theSingleton;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public boolean isStepFreeAccess() {
        return stepFreeAccess;
    }

    public void setStepFreeAccess(boolean stepFreeAccess) {
        this.stepFreeAccess = stepFreeAccess;
    }

    public boolean isAccessibleToilets() {
        return accessibleToilets;
    }

    public void setAccessibleToilets(boolean accessibleToilets) {
        this.accessibleToilets = accessibleToilets;
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public boolean isGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public boolean isDairyFree() {
        return dairyFree;
    }

    public void setDairyFree(boolean dairyFree) {
        this.dairyFree = dairyFree;
    }

    public FilterModel(String search, int distance) {
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
