package com.example.abbieturner.restaurantsfinder.FirebaseModels;

import android.graphics.Bitmap;

import java.util.UUID;

public class Restaurant {
    private String id, name, address, phone, menu, web, pictureUrl, cuisine;
    private Double lng, lat, rating;
    private Boolean delivery, stepsFreeAccess, accessibleToilets, vegan, vegetarian, glutenFree, dairyFree;
    private Bitmap picture;


    public Restaurant() {
        this.id = UUID.randomUUID().toString();
        lat = null;
        lng = null;
        delivery = false;
        stepsFreeAccess = false;
        accessibleToilets = false;
        vegan = false;
        vegetarian = false;
        glutenFree = false;
        dairyFree = false;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Boolean getDelivery() {
        return delivery;
    }

    public void setDelivery(Boolean delivery) {
        this.delivery = delivery;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public boolean hasPicture() {
        return picture != null;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public boolean isLocationSet() {
        return lat != null && lng != null;
    }

    public Boolean getStepsFreeAccess() {
        return stepsFreeAccess;
    }

    public void setStepsFreeAccess(Boolean stepsFreeAccess) {
        this.stepsFreeAccess = stepsFreeAccess;
    }

    public Boolean getAccessibleToilets() {
        return accessibleToilets;
    }

    public void setAccessibleToilets(Boolean accessibleToilets) {
        this.accessibleToilets = accessibleToilets;
    }

    public Boolean getVegan() {
        return vegan;
    }

    public void setVegan(Boolean vegan) {
        this.vegan = vegan;
    }

    public Boolean getVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(Boolean vegetarial) {
        this.vegetarian = vegetarial;
    }

    public Boolean getGlutenFree() {
        return glutenFree;
    }

    public void setGlutenFree(Boolean glutenFree) {
        this.glutenFree = glutenFree;
    }

    public Boolean getDairyFree() {
        return dairyFree;
    }

    public void setDairyFree(Boolean dairyFree) {
        this.dairyFree = dairyFree;
    }

    public boolean hasPictureUrl(){
        return pictureUrl != null && !pictureUrl.isEmpty();
    }
}