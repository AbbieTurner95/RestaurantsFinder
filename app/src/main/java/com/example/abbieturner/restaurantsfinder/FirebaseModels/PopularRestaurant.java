package com.example.abbieturner.restaurantsfinder.FirebaseModels;

public class PopularRestaurant {
    private String restaurantId;
    private int count;
    private String name;

    public PopularRestaurant(String id, String name){
        this.restaurantId = id;
        this.name = name;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void increateCountByOne(){
        count = (count + 1);
    }

    public void decreaseCountByOne(){
        count = (count -1);
    }

    public boolean hasMultipleLikes(){
        return count > 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
