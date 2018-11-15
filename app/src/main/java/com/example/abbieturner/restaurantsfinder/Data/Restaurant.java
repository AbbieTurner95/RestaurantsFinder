package com.example.abbieturner.restaurantsfinder.Data;

public class Restaurant {

    public String id;
    public String name;
    public String url;
    public Location locationList;
    public int price_range;
    public String photos_url;
    public String menu_url;
  //  public Rating userRatingList;
    public int online_delivery;
    public String cuisines;

    public Restaurant(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }

    public int getPrice_range() {
        return price_range;
    }

    public void setPrice_range(int price_range) {
        this.price_range = price_range;
    }

    public String getPhotos_url() {
        return photos_url;
    }

    public void setPhotos_url(String photos_url) {
        this.photos_url = photos_url;
    }

    public String getMenu_url() {
        return menu_url;
    }

    public void setMenu_url(String menu_url) {
        this.menu_url = menu_url;
    }

    public int getOnline_delivery() {
        return online_delivery;
    }

    public void setOnline_delivery(int online_delivery) {
        this.online_delivery = online_delivery;
    }

    public Location getLocation() {
        return locationList;
    }

    public void setLocation(Location locationList) {
        this.locationList = locationList;
    }
}
