package com.example.abbieturner.restaurantsfinder.FirebaseModels;

import android.graphics.Bitmap;

public class User {
    private String id;
    private String name;
    private String memberSince;
    private int numberOfReviews;
    private Bitmap picture;
    private String pictureUrl;

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

    public String getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(String memberSince) {
        this.memberSince = memberSince;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public void setNumberOfReviews(int numberOfReviews) {
        this.numberOfReviews = numberOfReviews;
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

    public String getMemberStatus(){
        if(numberOfReviews < 5){
            return "Bronze";
        }else if(numberOfReviews < 10){
            return "Silver";
        }else{
            return "Gold";
        }
    }
}
