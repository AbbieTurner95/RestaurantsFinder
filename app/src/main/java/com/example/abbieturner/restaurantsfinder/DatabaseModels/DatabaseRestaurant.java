package com.example.abbieturner.restaurantsfinder.DatabaseModels;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.example.abbieturner.restaurantsfinder.Data.Restaurant;

@Entity (tableName = "restaurantsDatabase")
public class DatabaseRestaurant {

    /////////////////////////////////////////////////////
    ////////// --- RESTAURANT PARAMETERS --- ////////////
    /////////////////////////////////////////////////////
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String url;
    private String cuisines;
    private int average_cost_for_two;
    private int price_range;
    private String currency;
    private String photos_url;
    private String menu_url;
    private String featured_image;
    private int has_online_delivery;


    /////////////////////////////////////////////////////
    ////////// --- LOCATION PARAMETERS --- //////////////
    /////////////////////////////////////////////////////

    private String address;
    private String locality;
    private String city;
    private int city_id;
    private String latitude;
    private String longitude;
    private String zipcode;
    private int country_id;
    private String locality_verbose;


    /////////////////////////////////////////////////////
    ////////// --- RATING PARAMETERS --- ////////////////
    /////////////////////////////////////////////////////

    private String aggregate_rating;
    private String rating_text;
    private String rating_color;
    private String votes;

    public DatabaseRestaurant(){

    }

    public DatabaseRestaurant(Restaurant restaurant){
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.url = restaurant.getUrl();
        this.cuisines = restaurant.getCuisines();
        this.average_cost_for_two = restaurant.getAverage_cost_for_two();
        this.price_range = restaurant.getPrice_range();
        this.currency = restaurant.getCurrency();
        this.photos_url = restaurant.getPhotos_url();
        this.menu_url = restaurant.getMenu_url();
        this.featured_image = restaurant.getFeatured_image();
        this.has_online_delivery = restaurant.getHas_online_delivery();

        this.address = restaurant.getLocation().getAddress();
        this.locality = restaurant.getLocation().getLocality();
        this.city = restaurant.getLocation().getLocality();
        this.city_id = restaurant.getLocation().getCity_id();
        this.latitude = restaurant.getLocation().getLatitude();
        this.longitude = restaurant.getLocation().getLongitude();
        this.zipcode = restaurant.getLocation().getZipcode();
        this.country_id = restaurant.getLocation().getCountry_id();
        this.locality_verbose = restaurant.getLocation().getLocality_verbose();

        this.aggregate_rating = restaurant.getUser_rating().getAggregate_rating();
        this.rating_text = restaurant.getUser_rating().getRating_text();
        this.rating_color = restaurant.getUser_rating().getRating_color();
        this.votes = restaurant.getUser_rating().getVotes();
    }


    /////////////////////////////////////////////////////
    ////////// --- RESTAURANT FUNCTIONS --- /////////////
    /////////////////////////////////////////////////////

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

    /////////////////////////////////////////////////////
    ////////// --- LOCATION FUNCTIONS --- ///////////////
    /////////////////////////////////////////////////////


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public String getLocality_verbose() {
        return locality_verbose;
    }

    public void setLocality_verbose(String locality_verbose) {
        this.locality_verbose = locality_verbose;
    }

    /////////////////////////////////////////////////////
    ////////// --- RATING FUNCTIONS --- /////////////////
    /////////////////////////////////////////////////////

    public String getAggregate_rating() {
        return aggregate_rating;
    }

    public void setAggregate_rating(String aggregate_rating) {
        this.aggregate_rating = aggregate_rating;
    }

    public String getRating_text() {
        return rating_text;
    }

    public void setRating_text(String rating_text) {
        this.rating_text = rating_text;
    }

    public String getRating_color() {
        return rating_color;
    }

    public void setRating_color(String rating_color) {
        this.rating_color = rating_color;
    }

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }
}