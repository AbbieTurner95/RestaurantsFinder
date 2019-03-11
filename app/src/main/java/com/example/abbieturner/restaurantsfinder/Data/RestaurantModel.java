package com.example.abbieturner.restaurantsfinder.Data;

import com.example.abbieturner.restaurantsfinder.FirebaseModels.Restaurant;

public class RestaurantModel {
    private boolean isFirebaseRestaurant;
    private Restaurant restaurantF;
    private com.example.abbieturner.restaurantsfinder.Data.Restaurant restaurantZ;

    public RestaurantModel(Restaurant restaurant) {
        this.restaurantF = restaurant;
        this.isFirebaseRestaurant = true;
    }

    public RestaurantModel(com.example.abbieturner.restaurantsfinder.Data.Restaurant restaurant) {
        this.restaurantZ = restaurant;
        this.isFirebaseRestaurant = false;
    }

    public Restaurant getFirebaseRestaurant() {
        return restaurantF;
    }

    public com.example.abbieturner.restaurantsfinder.Data.Restaurant getZomatoRestaurant() {
        return restaurantZ;
    }

    public boolean isFirebaseRestaurant() {
        return isFirebaseRestaurant;
    }

    public boolean isMenuSet(){
        if(isFirebaseRestaurant){
            return restaurantF.getMenu() != null && !restaurantF.getMenu().isEmpty();
        }else{
            return  restaurantZ.getMenu_url() != null && !restaurantZ.getMenu_url().isEmpty();
        }
    }

    public String getMenuUrl(){
        if(isFirebaseRestaurant){
            return restaurantF.getMenu();
        }else{
            return restaurantZ.getMenu_url();
        }
    }

    public boolean isWebUrlSet(){
        if(isFirebaseRestaurant){
            return restaurantF.getWeb() != null && !restaurantF.getWeb().isEmpty();
        }else{
            return restaurantZ.getUrl() != null && !restaurantZ.getUrl().isEmpty();
        }
    }

    public String getWebUrl(){
        if(isFirebaseRestaurant){
            return restaurantF.getWeb();
        }else{
            return restaurantZ.getUrl();
        }
    }

    public String getId(){
        if(isFirebaseRestaurant){
            return restaurantF.getId();
        }else{
            return restaurantZ.getId();
        }
    }

    public String getName(){
        if(isFirebaseRestaurant){
            return restaurantF.getName();
        }else{
            return restaurantZ.getName();
        }
    }

    public boolean hasDelivery() {
        if (isFirebaseRestaurant) {
            return restaurantF.getDelivery();
        } else {
            return restaurantZ.getHas_online_delivery() == 1;
        }
    }

    public boolean hasStepFreeAccess() {
        if (isFirebaseRestaurant) {
            return restaurantF.getStepsFreeAccess();
        } else {
            return false;
        }
    }

    public boolean hasAccessibleToilets() {
        if (isFirebaseRestaurant) {
            return restaurantF.getAccessibleToilets();
        } else {
            return false;
        }
    }

    public boolean isVegan() {
        if (isFirebaseRestaurant) {
            return restaurantF.getVegan();
        } else {
            return false;
        }
    }

    public boolean isVegetarian() {
        if (isFirebaseRestaurant) {
            return restaurantF.getVegetarian();
        } else {
            return false;
        }
    }

    public boolean isGlutenFree() {
        if (isFirebaseRestaurant) {
            return restaurantF.getGlutenFree();
        } else {
            return false;
        }
    }

    public boolean isDiaryFree() {
        if (isFirebaseRestaurant) {
            return restaurantF.getDairyFree();
        } else {
            return false;
        }
    }

    public String getStepFreeAccess() {
        if (isFirebaseRestaurant) {
            return restaurantF.getStepsFreeAccess().equals(true) ? "Step Free Access: Yes" : "Step Free Access: No";
        } else {
            return "Step Free Access: not set";
        }
    }

    public String getAccessibleToilets() {
        if (isFirebaseRestaurant) {
            return restaurantF.getAccessibleToilets().equals(true) ? "Accessible Toilets: Yes" : "Accessible Toilets: No";
        } else {
            return "Accessible Toilets: not set";
        }
    }

    public String getVegan() {
        if (isFirebaseRestaurant) {
            return restaurantF.getVegan().equals(true) ? "Vegan Food: Yes" : "Vegan Food: No";
        } else {
            return "Vegan Food: not set";
        }
    }

    public String getVegetarian() {
        if (isFirebaseRestaurant) {
            return restaurantF.getVegetarian().equals(true) ? "Vegetarian Food: Yes" : "Vegetarian Food: No";
        } else {
            return "Vegetarian Food: not set";
        }
    }

    public String getGlutenFree() {
        if (isFirebaseRestaurant) {
            return restaurantF.getGlutenFree().equals(true) ? "Gluten Free Food: Yes" : "Gluten Free Food: No";
        } else {
            return "Gluten Free Food: not set";
        }
    }

    public String getDairyFree() {
        if (isFirebaseRestaurant) {
            return restaurantF.getDairyFree().equals(true) ? "Dairy Free Food: Yes" : "Dairy Free Food: No";
        } else {
            return "Dairy Free Food: not set";
        }
    }
}
