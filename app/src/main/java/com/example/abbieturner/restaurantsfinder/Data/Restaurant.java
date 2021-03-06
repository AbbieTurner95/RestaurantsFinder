package com.example.abbieturner.restaurantsfinder.Data;

import com.example.abbieturner.restaurantsfinder.DatabaseModels.DatabaseRestaurant;

public class Restaurant {

    private String id, name, url, cuisines, currency, photos_url, menu_url, featured_image;
    private int average_cost_for_two, price_range, has_online_delivery;
    private RestaurantLocationData location;
    private RestaurantUserRating user_rating;

    public Restaurant() {

    }

    public Restaurant(DatabaseRestaurant databaseRestaurant) {
        this.id = databaseRestaurant.getId();
        this.name = databaseRestaurant.getName();
        this.url = databaseRestaurant.getUrl();
        this.cuisines = databaseRestaurant.getCuisines();
        this.average_cost_for_two = databaseRestaurant.getAverage_cost_for_two();
        this.price_range = databaseRestaurant.getPrice_range();
        this.currency = databaseRestaurant.getCurrency();
        this.photos_url = databaseRestaurant.getPhotos_url();
        this.menu_url = databaseRestaurant.getMenu_url();
        this.featured_image = databaseRestaurant.getFeatured_image();
        this.has_online_delivery = databaseRestaurant.getHas_online_delivery();

        this.location = new RestaurantLocationData(
                databaseRestaurant.getAddress(),
                databaseRestaurant.getLocality(),
                databaseRestaurant.getCity(),
                databaseRestaurant.getCity_id(),
                databaseRestaurant.getLatitude(),
                databaseRestaurant.getLongitude(),
                databaseRestaurant.getZipcode(),
                databaseRestaurant.getCountry_id(),
                databaseRestaurant.getLocality_verbose());
        this.user_rating = new RestaurantUserRating(
                databaseRestaurant.getAggregate_rating(),
                databaseRestaurant.getRating_text(),
                databaseRestaurant.getRating_color(),
                databaseRestaurant.getVotes());
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

    public RestaurantLocationData getLocation() {
        return location;
    }

    public void setLocation(RestaurantLocationData location) {
        this.location = location;
    }

    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }

    public int getAverage_cost_for_two() {
        return average_cost_for_two;
    }

    public void setAverage_cost_for_two(int average_cost_for_two) {
        this.average_cost_for_two = average_cost_for_two;
    }

    public int getPrice_range() {
        return price_range;
    }

    public void setPrice_range(int price_range) {
        this.price_range = price_range;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public RestaurantUserRating getUser_rating() {
        return user_rating;
    }

    public void setUser_rating(RestaurantUserRating user_rating) {
        this.user_rating = user_rating;
    }

    public String getPhotos_url() {
        if (photos_url == null || photos_url.isEmpty()) {
            return "url not set";
        } else {
            return photos_url;
        }
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

    public String getFeatured_image() {
        return featured_image;
    }

    public void setFeatured_image(String featured_image) {
        this.featured_image = featured_image;
    }

    public int getHas_online_delivery() {
        return has_online_delivery;
    }

    public void setHas_online_delivery(int has_online_delivery) {
        this.has_online_delivery = has_online_delivery;
    }

    public boolean isLocationSet() {
        return location != null
                && !location.getLongitude().isEmpty()
                && !location.getLongitude().isEmpty();
    }
}